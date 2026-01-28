package com.example.planner.data.dto.tracking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class TrackingCreationDto(
    val date: LocalDate,
    val hours: Double,
    @Json(name = "project_id")
    val projectId: Long,
    @Json(name = "task_id")
    val taskId: Long
)