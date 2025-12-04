package com.example.planner.data.dto.task

import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskUrgency


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateTaskInfoRequestDto(
    var name: String,
    var urgency: TaskUrgency,
    var complexity: TaskComplexity,
    var projectId: Long,
    var description: String,
)