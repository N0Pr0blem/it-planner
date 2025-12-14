package com.laba.it_planner.dto.task

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.dto.userInfo.UserInfoForTaskDto
import com.laba.it_planner.model.task.TaskComplexity
import com.laba.it_planner.model.task.TaskDetails
import com.laba.it_planner.model.task.TaskStatus
import com.laba.it_planner.model.task.TaskUrgency
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