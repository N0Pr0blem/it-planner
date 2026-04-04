package com.laba.it_planner.service.ai.impl

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.laba.it_planner.config.AIConfig
import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.ai.AIRequestDto
import com.laba.it_planner.dto.ai.AIRequestResponseDto
import com.laba.it_planner.dto.ai.AISavedResponseDto
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.ai.AIRequest
import com.laba.it_planner.model.ai.AIRequestPattern
import com.laba.it_planner.model.ai.AIResponse
import com.laba.it_planner.repository.ai.AIRequestRepository
import com.laba.it_planner.repository.ai.AIResponseRepository
import com.laba.it_planner.repository.projection.AIResponseProjection
import com.laba.it_planner.service.ai.AIRequestService
import com.laba.it_planner.service.user.UserInfoService
import com.laba.it_planner.utils.feature.FeatureToggleService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.io.IOException
import java.security.Principal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.logging.Logger

@Service
class AIRequestServiceImpl(
    private val aiRequestRepository: AIRequestRepository,
    private val aiResponseRepository: AIResponseRepository,
    private val userInfoService: UserInfoService,
    private val aiConfig: AIConfig,
    private val featureToggleService: FeatureToggleService,
    private val messageSource: MessageSource
) : AIRequestService {

    private val logger: Logger = Logger.getLogger(AIRequestServiceImpl::class.java.name)
    private val gson = Gson()

    override fun sendRequest(
        aIRequestDto: AIRequestDto,
        principal: Principal
    ): AIRequestResponseDto {
        var result: AIRequestResponseDto
        if (aIRequestDto.request.length < aiConfig.minRequestLength) {
            logger.warning("Count of ai request symbols ${aIRequestDto.request.length}")
            throw DataException("error.ai.symbols.count", "");
        }

        if (featureToggleService.isEnabled("ai.sending")) {
            if (featureToggleService.isEnabled("ai.request.limit") && isMoreThanLimit(principal)) {
                throw DataException("error.ai.request.limit", aiConfig.defaultLimitPerDay.toString())
            }
            val startTime = System.currentTimeMillis()

            val prompt = formatPrompt(aIRequestDto.pattern, aIRequestDto.request)
            logger.info("Generated prompt: $prompt")

            val aiResponse = sendToOpenAI(prompt)

            val request = saveRequestToDatabase(aIRequestDto, principal)

            val processingTime = System.currentTimeMillis() - startTime

            result = AIRequestResponseDto(
                requestId = request.id!!,
                response = aiResponse,
                pattern = aIRequestDto.pattern,
                originalRequest = aIRequestDto.request,
                processingTimeMs = processingTime
            )
        } else {
            result = AIRequestResponseDto(
                requestId = 0,
                response = aIRequestDto.request,
                pattern = aIRequestDto.pattern,
                originalRequest = aIRequestDto.request,
                processingTimeMs = 0
            )
        }

        return result
    }

    override fun saveResponse(
        aIRequestDto: AIRequestResponseDto,
        locale: Locale
    ): MessageResponseDto {
        val request = aiRequestRepository.findById(aIRequestDto.requestId)
            .orElseThrow { DataException("error.ai.request.id", aIRequestDto.requestId) }
        val response = AIResponse(
            request = request,
            response = aIRequestDto.response,
            processingTimeMs = aIRequestDto.processingTimeMs
        )
        aiResponseRepository.save(response)
        return MessageResponseDto(messageSource.getMessage("message.ai.response.successfully_saved", null, locale))
    }

    override fun getAll(principal: Principal): List<AISavedResponseDto> {
        val aiResponseProjections = aiResponseRepository.getAllByUsername(principal.name)
        return toAIResponse(aiResponseProjections)
    }

    private fun toAIResponse(aiResponseProjections: List<AIResponseProjection>): List<AISavedResponseDto> {
        val result = ArrayList<AISavedResponseDto>()
        aiResponseProjections.forEach { it ->
            result.add(
                AISavedResponseDto(
                    responseId = it.getResponseId(),
                    request = it.getRequest(),
                    response = it.getResponse(),
                    pattern = AIRequestPattern.fromString(it.getPattern()),
                    processingTimeMs = it.getProcessingTime(),
                    sendDate = it.getSendDate(),
                )
            )
        }

        return result
    }

    private fun formatPrompt(pattern: AIRequestPattern, userRequest: String): String {
        return when (pattern) {
            AIRequestPattern.CUSTOM -> userRequest
            AIRequestPattern.IMPROVE_TASK_DESCRIPTION ->
                String.format(pattern.value, userRequest)
        }
    }

    private fun sendToOpenAI(prompt: String): String {
        val payload = mapOf(
            "model" to aiConfig.openAiApiModel,
            "input" to listOf(
                mapOf(
                    "role" to "system",
                    "content" to listOf(
                        mapOf("type" to "input_text", "text" to "Ты полезный IT-эксперт.")
                    )
                ),
                mapOf(
                    "role" to "user",
                    "content" to listOf(
                        mapOf("type" to "input_text", "text" to prompt)
                    )
                )
            )
        )

        val jsonBody = gson.toJson(payload)

        val requestBody = RequestBody.create(
            "application/json".toMediaType(),
            jsonBody
        )

        val request = Request.Builder()
            .url(aiConfig.openAiApiUrl)
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${aiConfig.openAiApiKey}")
            .addHeader("Content-Type", "application/json")
            .build()

        return try {
            logger.info("Sending request to OpenAI via proxy 127.0.0.1:8081...")
            aiConfig.client().newCall(request).execute().use { response ->

                val responseBody = response.body?.string()
                    ?: throw RuntimeException("Empty response from OpenAI")

                if (!response.isSuccessful) {
                    logger.severe("OpenAI error ${response.code}: $responseBody")
                    throw RuntimeException("OpenAI API error ${response.code}: $responseBody")
                }

                parseResponse(responseBody)
            }

        } catch (e: IOException) {
            logger.severe("Network error: ${e.message}")
            throw RuntimeException("Failed to call OpenAI: ${e.message}", e)
        }
    }

    private fun parseResponse(responseBody: String): String {
        val json = gson.fromJson(responseBody, JsonObject::class.java)

        val output = json.getAsJsonArray("output")
            ?: throw RuntimeException("Invalid OpenAI response: no output")

        if (output.size() == 0) {
            throw RuntimeException("Invalid OpenAI response: output array is empty")
        }

        val messageElement = output.firstOrNull {
            it.asJsonObject.has("type") && it.asJsonObject.get("type").asString == "message"
        }?.asJsonObject ?: throw RuntimeException("Invalid OpenAI response: no message element")

        val content = messageElement.getAsJsonArray("content")
            ?: throw RuntimeException("Invalid OpenAI response: no content in message")

        if (content.size() == 0) {
            throw RuntimeException("Invalid OpenAI response: content array is empty")
        }

        val textElement = content.firstOrNull {
            it.asJsonObject.has("type") && it.asJsonObject.get("type").asString == "output_text"
        }?.asJsonObject ?: throw RuntimeException("Invalid OpenAI response: no output_text in content")

        return textElement.get("text")?.asString
            ?: throw RuntimeException("Invalid OpenAI response: no text field")
    }

    private fun saveRequestToDatabase(
        aIRequestDto: AIRequestDto,
        principal: Principal
    ): AIRequest {

        val userInfo = userInfoService.getUserInfo(principal.name)

        val entity = AIRequest(
            pattern = aIRequestDto.pattern,
            request = aIRequestDto.request,
            sendDate = LocalDateTime.now(),
            author = userInfo
        )

        logger.info("Saved AI request for user ${principal.name}")
        return aiRequestRepository.save(entity)
    }

    private fun isMoreThanLimit(principal: Principal): Boolean {
        return aiRequestRepository.getAllUsersRequestsByDate(
            principal.name,
            LocalDate.now(),
            LocalDate.now().plusDays(1)
        ).size >= aiConfig.defaultLimitPerDay
    }
}