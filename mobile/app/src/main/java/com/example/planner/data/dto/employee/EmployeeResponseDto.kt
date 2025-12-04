package com.example.planner.data.dto.employee

import com.example.planner.data.model.user.OauthUser
import com.example.planner.data.model.user.ProjectRole
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeResponseDto(
    val id:Long,
    val projectRole: ProjectRole,
    var user: OauthUser
)
