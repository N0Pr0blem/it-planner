package com.example.planner.data.dto.task

import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.data.model.task.TaskUrgency

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UpdateTaskInfoRequestDto(
    var name: String? = null,
    var urgency: TaskUrgency? = null,
    var complexity: TaskComplexity? = null,
    var status: TaskStatus? = null,
    var description: String? = null,
)