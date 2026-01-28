package com.example.planner.data.dto.task

import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskUrgency
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateTaskInfoRequestDto(
    var name: String,
    var urgency: TaskUrgency,
    var complexity: TaskComplexity,
    @Json(name = "project_id")
    var projectId: Long,
    var description: String,
)