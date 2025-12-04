package com.example.planner.data.dto.tracking

import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TrekkingCreationDto(
    val date: LocalDate,
    val hours: Double,
    val projectId: Long,
    val taskId: Long
)