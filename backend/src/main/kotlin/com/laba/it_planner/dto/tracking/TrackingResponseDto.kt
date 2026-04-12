package com.laba.it_planner.dto.tracking

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TrackingResponseDto(
    var id : Long,
    var date: LocalDate,
    var hours: Double,
    var employeeFirstName: String?,
    var employeeSecondName: String?,
    var taskDetailsId: Long,
    var taskName: String
)