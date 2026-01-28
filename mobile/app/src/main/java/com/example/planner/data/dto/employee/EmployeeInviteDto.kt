package com.example.planner.data.dto.employee

import com.example.planner.data.model.user.ProjectRole
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeInviteDto(
    val username: String,
    @Json(name = "project_role")
    val projectRole: ProjectRole,
)