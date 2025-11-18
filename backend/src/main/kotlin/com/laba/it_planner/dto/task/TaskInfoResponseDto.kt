package com.laba.it_planner.dto.task

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.model.task.TaskComplexity
import com.laba.it_planner.model.task.TaskUrgency
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TaskInfoResponseDto(
    val id: Long,
    var isCompleted: Boolean,
    var name: String,
    var urgency: TaskUrgency,
    var complexity: TaskComplexity,
    var creationDate: LocalDateTime,
    var projectId: Long,
)