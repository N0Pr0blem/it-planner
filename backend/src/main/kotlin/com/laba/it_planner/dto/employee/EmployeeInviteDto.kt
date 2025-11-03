package com.laba.it_planner.dto.employee;

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.model.user.ProjectRole

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class EmployeeInviteDto(
    val username: String,
    val projectRole: ProjectRole,
)