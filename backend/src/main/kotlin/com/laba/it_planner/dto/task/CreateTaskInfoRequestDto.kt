package com.laba.it_planner.dto.task

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.model.task.enum_old.TaskComplexity
import com.laba.it_planner.model.task.enum_old.TaskUrgency

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateTaskInfoRequestDto(
    var name: String,
    var urgency: TaskUrgency,
    var complexity: TaskComplexity,
    var projectId: Long,
    var description: String,
)