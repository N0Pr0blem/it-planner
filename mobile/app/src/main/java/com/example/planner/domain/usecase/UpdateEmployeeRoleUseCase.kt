package com.example.planner.domain.usecase

import com.example.planner.domain.model.ProjectMember
import com.example.planner.domain.model.ProjectRole
import com.example.planner.domain.repository.ProjectRepository

class UpdateEmployeeRoleUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long, employeeId: Long, role: ProjectRole): Result<ProjectMember> {
        return projectRepository.updateEmployeeRole(projectId, employeeId, role)
    }
}
