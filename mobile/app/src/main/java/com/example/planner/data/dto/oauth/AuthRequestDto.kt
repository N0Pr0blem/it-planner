package com.example.planner.data.dto.oauth


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AuthRequestDto (
    val username: String,
    val password: String
)