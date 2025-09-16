package com.laba.it_planner.dto.oauth

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RegisterRequestDto(
    val username: String,
    val password: String,
    val firstName: String
)