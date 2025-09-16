package com.laba.it_planner.controller

import com.laba.it_planner.dto.oauth.AuthRequestDto
import com.laba.it_planner.dto.oauth.AuthResponseDto
import com.laba.it_planner.dto.oauth.RegisterRequestDto
import com.laba.it_planner.dto.oauth.RegisterResponseDto
import com.laba.it_planner.mapper.RegisterMapper
import com.laba.it_planner.service.OauthService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class OauthController(
    private val oauthService: OauthService,
    private val registerMapper: RegisterMapper
) {
    @PostMapping("/register")
    @Operation(summary = "Register new user")
    fun register(@RequestBody registerRequestDto: RegisterRequestDto): ResponseEntity<RegisterResponseDto> {
        return ResponseEntity.ok(registerMapper.toDto(oauthService.register(registerRequestDto)))
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    fun login(@RequestBody authRequestDto: AuthRequestDto): AuthResponseDto {
        val tokenDetails = oauthService.authenticate(authRequestDto)
        return AuthResponseDto(
            token = tokenDetails.token,
            expiresAt = tokenDetails.expiresAt,
            issuedAt = tokenDetails.issuedAt
        )
    }
}