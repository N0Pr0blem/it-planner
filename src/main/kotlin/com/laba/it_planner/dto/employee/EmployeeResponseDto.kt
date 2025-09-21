package com.laba.it_planner.dto.employee

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.model.user.ProjectRole

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class EmployeeResponseDto(
    val id:Long,
    val projectRole: ProjectRole,
    var user: OauthUser
)
