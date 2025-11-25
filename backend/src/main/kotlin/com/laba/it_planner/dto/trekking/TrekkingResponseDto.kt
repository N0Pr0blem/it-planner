package com.laba.it_planner.dto.trekking

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TrekkingResponseDto(
    val date: LocalDate,
    val hours: Double,
    val employeeId: Long,
    val taskDetailsId: Long
)