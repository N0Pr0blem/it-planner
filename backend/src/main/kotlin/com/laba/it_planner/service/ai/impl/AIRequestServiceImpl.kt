package com.laba.it_planner.service.ai.impl

import com.google.gson.Gson
import com.laba.it_planner.dto.ai.AIRequestDto
import com.laba.it_planner.dto.ai.AIRequestResponseDto
import com.laba.it_planner.model.ai.AIRequest
import com.laba.it_planner.model.ai.AIRequestPattern
import com.laba.it_planner.repository.ai.AIRequestRepository
import com.laba.it_planner.service.ai.AIRequestService
import com.laba.it_planner.service.user.UserInfoService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.security.Principal
import java.time.LocalDateTime
import java.util.logging.Logger

@Service
class AIRequestServiceImpl(
    private val aiRequestRepository: AIRequestRepository,
    private val userInfoService: UserInfoService,

    @Value("\${openai.api.key}")
    private val OPEN_AI_API_KEY: String
) : AIRequestService {
    private val logger: Logger = Logger.getLogger(AIRequestServiceImpl::class.java.name)
    private val client = OkHttpClient()
    private val gson = Gson()

    override fun sendRequest(
        aIRequestDto: AIRequestDto,
        principal: Principal
    ): AIRequestResponseDto {
        val startTime = System.currentTimeMillis()

        val prompt = formatPrompt(aIRequestDto.pattern, aIRequestDto.request)
        logger.info("Generated prompt for pattern ${aIRequestDto.pattern}: $prompt")

        val aiResponse = sendToOpenAI(prompt)

        val request = saveRequestToDatabase(aIRequestDto, principal)

        val processingTime = System.currentTimeMillis() - startTime
        logger.info("AI request processed in ${processingTime}ms")

        return AIRequestResponseDto(
            requestId = request.id!!,
            response = aiResponse,
            pattern = aIRequestDto.pattern,
            originalRequest = aIRequestDto.request,
            processingTimeMs = processingTime
        )
    }

    private fun formatPrompt(pattern: AIRequestPattern, userRequest: String): String {
        return when (pattern) {
            AIRequestPattern.CUSTOM -> userRequest
            AIRequestPattern.IMPROVE_TASK_DESCRIPTION ->
                String.format(pattern.value, userRequest)
        }
    }

    private fun sendToOpenAI(prompt: String): String {
        val url = "https://api.openai.com/v1/responses"

        val payload = mapOf(
            "model" to "gpt-5-nano",
            "messages" to listOf(
                mapOf("role" to "system", "content" to "Ты полезный IT-эксперт."),
                mapOf("role" to "user", "content" to prompt)
            )
        )

        val jsonBody = gson.toJson(payload)

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonBody
        )

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $OPEN_AI_API_KEY")
            .addHeader("Content-Type", "application/json")
            .build()

        return try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful) {
                logger.severe("OpenAI API error: ${response.code} - $responseBody")
                throw RuntimeException("OpenAI API returned error ${response.code}: $responseBody")
            }

            val jsonResponse = gson.fromJson(responseBody, Map::class.java)
            val choices = jsonResponse["choices"] as List<*>
            val firstChoice = choices[0] as Map<*, *>
            val message = firstChoice["message"] as Map<*, *>
            message["content"] as String

        } catch (e: IOException) {
            logger.severe("Network error while calling OpenAI: ${e.message}")
            throw RuntimeException("Failed to communicate with OpenAI", e)
        }
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
}