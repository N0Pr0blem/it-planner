package com.example.planner.data.dto.employee

import com.example.planner.data.model.user.ProjectRole
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeUpdateRoleDto (val projectRole: ProjectRole)
