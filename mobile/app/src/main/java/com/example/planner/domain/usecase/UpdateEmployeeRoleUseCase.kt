package com.example.planner.domain.usecase

import com.example.planner.domain.exception.ValidationException
import com.example.planner.domain.model.User
import com.example.planner.domain.repository.ProjectRepository

class UpdateEmployeeRoleUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long, employeeId: Long, role: String): Result<User> {
        // Валидация параметров
        if (role.isBlank()) {
            return Result.failure(ValidationException("Role cannot be empty"))
        }
        
        // Дополнительная валидация
        if (role.length > 50) {
            return Result.failure(ValidationException("Role cannot exceed 50 characters"))
        }
        
        return projectRepository.updateEmployeeRole(projectId, employeeId, role)
    }
}