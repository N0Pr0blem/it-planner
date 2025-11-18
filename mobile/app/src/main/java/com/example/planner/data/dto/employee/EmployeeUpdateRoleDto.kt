package com.example.planner.data.dto.employee

import com.example.planner.data.model.user.ProjectRole

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class EmployeeUpdateRoleDto (val projectRole: ProjectRole)
