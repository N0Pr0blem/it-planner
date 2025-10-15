package com.laba.it_planner.dto.oauth

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AuthResponseDto (
    val token: String? = null,
    val issuedAt: Date? = null,
    val expiresAt: Date? = null,
)