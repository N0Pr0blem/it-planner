package com.laba.it_planner.dto.task

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.model.task.TaskComplexity
import com.laba.it_planner.model.task.TaskStatus
import com.laba.it_planner.model.task.TaskUrgency

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UpdateTaskInfoRequestDto(
    var name: String? = null,
    var urgency: TaskUrgency? = null,
    var complexity: TaskComplexity? = null,
    var status: TaskStatus? = null,
    var description: String? = null,
)