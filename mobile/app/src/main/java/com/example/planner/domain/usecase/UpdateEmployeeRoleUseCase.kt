package com.example.planner.domain.usecase

import com.example.planner.domain.model.User
import com.example.planner.domain.repository.ProjectRepository

class UpdateEmployeeRoleUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long, employeeId: Long, role: String): Result<User> {
        // Можно добавить валидацию параметров
        if (role.isBlank()) {
            return Result.failure(IllegalArgumentException("Role cannot be empty"))
        }
        
        return projectRepository.updateEmployeeRole(projectId, employeeId, role)
    }
}