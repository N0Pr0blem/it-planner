package com.example.planner.data.dto.tracking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class TrackingResponseDto(
    var id : Long,
    var date: LocalDate,
    var hours: Double,
    @Json(name = "employee_first_name")
    var employeeFirstName: String?,
    @Json(name = "employee_second_name")
    var employeeSecondName: String?,
    @Json(name = "task_details_id")
    var taskDetailsId: Long
)