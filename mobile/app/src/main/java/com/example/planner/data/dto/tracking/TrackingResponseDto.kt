package com.example.planner.data.dto.tracking

import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TrackingResponseDto(
    var id : Long,
    var date: LocalDate,
    var hours: Double,
    var employeeFirstName: String?,
    var employeeSecondName: String?,
    var taskDetailsId: Long
)