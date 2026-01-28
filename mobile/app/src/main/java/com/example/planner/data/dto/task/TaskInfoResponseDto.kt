package com.example.planner.data.dto.task

import com.example.planner.data.dto.userInfo.UserInfoForTaskDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class TaskInfoResponseDto(
    val id: Long,
    @Json(name = "is_completed")
    var isCompleted: Boolean,
    var name: String,
    var urgency: String,
    var complexity: String,
    @Json(name = "creation_date")
    var creationDate: LocalDateTime,
    @Json(name = "project_id")
    var projectId: Long,
    var status: String,
    @Json(name = "assigned_by")
    val assignedBy: UserInfoForTaskDto?,
    @Json(name = "assigned_to")
    val assignedTo: UserInfoForTaskDto?
)