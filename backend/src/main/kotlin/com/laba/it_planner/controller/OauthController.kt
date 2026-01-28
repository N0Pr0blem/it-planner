package com.laba.it_planner.controller

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.oauth.AuthRequestDto
import com.laba.it_planner.dto.oauth.AuthResponseDto
import com.laba.it_planner.dto.oauth.RegisterRequestDto
import com.laba.it_planner.dto.oauth.RegisterResponseDto
import com.laba.it_planner.mapper.RegisterMapper
import com.laba.it_planner.service.OauthService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class OauthController(
    private val oauthService: OauthService,
    private val registerMapper: RegisterMapper
) {
    @PostMapping("/register")
    @Operation(summary = "Register new user")
    fun register(@RequestBody registerRequestDto: RegisterRequestDto): ResponseEntity<RegisterResponseDto> {
        println("Register request received for user ${registerRequestDto.username}")
        return ResponseEntity.ok(registerMapper.toDto(oauthService.register(registerRequestDto)))
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    fun login(@RequestBody authRequestDto: AuthRequestDto): AuthResponseDto {
        println("Login request received for user ${authRequestDto.username}") //TODO make a logger
        val tokenDetails = oauthService.authenticate(authRequestDto)
        return AuthResponseDto(
            token = tokenDetails.token,
            expiresAt = tokenDetails.expiresAt,
            issuedAt = tokenDetails.issuedAt
        )
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify user by code from email")
    fun verify(@RequestParam("code") code: String,
               @RequestParam("username") username: String
    ): ResponseEntity<MessageResponseDto> {
        println("Try verify ${username}: $code")
        return ResponseEntity.ok(oauthService.verify(username, code))
    }

    @PostMapping("/resend")
    @Operation(summary = "Resend verification code")
    fun resend(@RequestParam("username") username: String): ResponseEntity<MessageResponseDto> {
        println("Resend verification code for $username")
        return ResponseEntity.ok(oauthService.resendVerificationCode(username))
    }
}
