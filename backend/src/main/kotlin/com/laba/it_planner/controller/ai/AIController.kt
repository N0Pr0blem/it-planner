package com.laba.it_planner.controller.ai

import com.laba.it_planner.controller.project.ProjectController
import com.laba.it_planner.dto.ai.AIRequestDto
import com.laba.it_planner.dto.ai.AIRequestResponseDto
import com.laba.it_planner.dto.project.ProjectCreateRequestDto
import com.laba.it_planner.dto.project.ProjectCreateResponseDto
import com.laba.it_planner.service.ai.AIRequestService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/ai")
class AIController (
    private val aiRequestService: AIRequestService,
){
    private val logger: Logger = Logger.getLogger(AIController::class.java.name)

    @PostMapping()
    @Operation(summary = "Send request to AI model")
    fun createProject(@Valid @RequestBody aIRequestDto: AIRequestDto, principal: Principal): ResponseEntity<AIRequestResponseDto> {
        logger.info("EVENT_SEND_AI_REQUEST | Sending request to AI model")
        val response = aiRequestService.sendRequest(aIRequestDto, principal)
        logger.info("EVENT_SEND_AI_RESPONSE | Getting response from AI model")
        return ResponseEntity.ok(response)
    }
}