package com.example.planner.data.dto.oauth

import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AuthResponseDto (
    val token: String? = null,
    val issuedAt: Date? = null,
    val expiresAt: Date? = null,
)