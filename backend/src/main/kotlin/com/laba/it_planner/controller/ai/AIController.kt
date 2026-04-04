package com.laba.it_planner.controller.ai

import com.laba.it_planner.controller.project.ProjectController
import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.ai.AIRequestDto
import com.laba.it_planner.dto.ai.AIRequestResponseDto
import com.laba.it_planner.dto.ai.AISavedResponseDto
import com.laba.it_planner.dto.project.ProjectCreateRequestDto
import com.laba.it_planner.dto.project.ProjectCreateResponseDto
import com.laba.it_planner.service.ai.AIRequestService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.Locale
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/ai")
class AIController (
    private val aiRequestService: AIRequestService,
){
    private val logger: Logger = Logger.getLogger(AIController::class.java.name)

    @PostMapping()
    @Operation(summary = "Send request to AI model")
    fun sendRequest(@Valid @RequestBody aIRequestDto: AIRequestDto, principal: Principal): ResponseEntity<AIRequestResponseDto> {
        logger.info("EVENT_SEND_AI_REQUEST | Sending request to AI model")
        val response = aiRequestService.sendRequest(aIRequestDto, principal)
        logger.info("EVENT_SEND_AI_RESPONSE | Getting response from AI model")
        return ResponseEntity.ok(response)
    }

    @PostMapping("/response")
    @Operation(summary = "Save response from AI model")
    fun saveResponse(@Valid @RequestBody aIRequestDto: AIRequestResponseDto, locale: Locale): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_SAVE_AI_RESPONSE | Save response from AI model")
        val response = aiRequestService.saveResponse(aIRequestDto, locale)
        logger.info("EVENT_SAVE_AI_RESPONSE | End saving response from AI model")
        return ResponseEntity.ok(response)
    }

    @GetMapping("/response")
    @Operation(summary = "Get all response from AI model")
    fun getAllResponse(principal: Principal): ResponseEntity<List<AISavedResponseDto>> {
        logger.info("EVENT_GET_ALL_RESPONSE | Get all responses from AI model")
        val response = aiRequestService.getAll(principal)
        logger.info("EVENT_GET_ALL_RESPONSE | End getting responses from AI model")
        return ResponseEntity.ok(response)
    }
}