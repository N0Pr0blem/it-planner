package com.example.planner.data.dto.employee

import com.example.planner.data.model.user.ProjectRole
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeInviteDto(
    val username: String,
    val projectRole: ProjectRole,
)