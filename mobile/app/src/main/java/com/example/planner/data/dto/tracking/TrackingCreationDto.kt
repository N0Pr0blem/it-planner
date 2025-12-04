package com.example.planner.data.dto.tracking

import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class TrackingCreationDto(
    val date: LocalDate,
    val hours: Double,
    val projectId: Long,
    val taskId: Long
)