package com.example.planner.data.dto.project

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProjectCreateRequestDto(
    val name: String
)
