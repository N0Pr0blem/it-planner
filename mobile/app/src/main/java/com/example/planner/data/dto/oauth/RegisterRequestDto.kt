package com.example.planner.data.dto.oauth

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RegisterRequestDto(
    val username: String,
    val password: String,
    val firstName: String
)