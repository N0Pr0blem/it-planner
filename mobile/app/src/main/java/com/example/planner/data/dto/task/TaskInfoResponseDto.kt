package com.example.planner.data.dto.task

import com.example.planner.data.dto.userInfo.UserInfoForTaskDto
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TaskInfoResponseDto(
    val id: Long,
    var isCompleted: Boolean,
    var name: String,
    var urgency: String,
    var complexity: String,
    var creationDate: LocalDateTime,
    var projectId: Long,
    var status: String,
    val assignedBy: UserInfoForTaskDto?,
    val assignedTo: UserInfoForTaskDto?
)