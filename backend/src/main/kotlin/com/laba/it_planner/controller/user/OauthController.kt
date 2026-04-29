package com.laba.it_planner.controller.user

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.oauth.AuthRequestDto
import com.laba.it_planner.dto.oauth.AuthResponseDto
import com.laba.it_planner.dto.oauth.RegisterRequestDto
import com.laba.it_planner.dto.oauth.RegisterResponseDto
import com.laba.it_planner.mapper.user.RegisterMapper
import com.laba.it_planner.service.user.OauthService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/auth")
class OauthController(
    private val oauthService: OauthService,
    private val registerMapper: RegisterMapper
) {
    private val logger: Logger = Logger.getLogger(OauthController::class.java.name)

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    fun register(@Valid @RequestBody registerRequestDto: RegisterRequestDto): ResponseEntity<RegisterResponseDto> {
        logger.info("EVENT_REGISTRATION | Start registration")
        val res = oauthService.register(registerRequestDto)
        logger.info("EVENT_REGISTRATION | Ending registration")
        return ResponseEntity.ok(registerMapper.toDto(res))
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    fun login(@RequestBody authRequestDto: AuthRequestDto): ResponseEntity<AuthResponseDto> {
        logger.info("EVENT_LOGGING | Start logging user")
        val tokenDetails = oauthService.authenticate(authRequestDto)
        val res = AuthResponseDto(
            token = tokenDetails.token,
            expiresAt = tokenDetails.expiresAt,
            issuedAt = tokenDetails.issuedAt
        )
        logger.info("EVENT_LOGGING | Ending logging user")
        return ResponseEntity.ok(res)
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify user by code from email")
    fun verify(@RequestParam("code") code: String,
               @RequestParam("username") username: String
    ): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_VERIFICATION | Start verify")
        val res = oauthService.verify(username, code)
        logger.info("EVENT_VERIFICATION | Ending verify")
        return ResponseEntity.ok(res)
    }

    @PostMapping("/recover")
    @Operation(summary = "get code for recover user password")
    fun recover(@RequestParam("username") username: String): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_SEND_RECOVER_CODE | Start sending code for user")
        val res = oauthService.recoverCode(username)
        logger.info("EVENT_SEND_RECOVER_CODE | End sending code for user")
        return ResponseEntity.ok(res)
    }

    @PatchMapping("/recover")
    @Operation(summary = "get code for recover user password")
    fun recoverPassword(@RequestParam("code") code: String,
                        @RequestParam("username") username: String,
                        @RequestParam("password") password: String,
    ): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_RECOVER_PASSWORD_CODE | Start recover password for user")
        val res = oauthService.recoverPassword(username, code, password)
        logger.info("EVENT_RECOVER_PASSWORD_CODE | End recover password for user")
        return ResponseEntity.ok(res)
    }
}