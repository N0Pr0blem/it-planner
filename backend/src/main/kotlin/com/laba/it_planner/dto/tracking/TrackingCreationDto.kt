package com.laba.it_planner.dto.tracking

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TrackingCreationDto(
    val date: LocalDate,
    val hours: Double,
    val projectId: Long,
    val taskId: Long
)