package com.example.planner.data.dto.tracking

import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class TrackingResponseDto(
    var id : Long,
    var date: LocalDate,
    var hours: Double,
    var employeeFirstName: String?,
    var employeeSecondName: String?,
    var taskDetailsId: Long
)