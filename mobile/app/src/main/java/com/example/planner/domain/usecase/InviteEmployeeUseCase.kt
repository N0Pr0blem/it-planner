package com.example.planner.domain.usecase

import com.example.planner.domain.exception.ValidationException
import com.example.planner.domain.model.User
import com.example.planner.domain.repository.ProjectRepository

class InviteEmployeeUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long, username: String, role: String): Result<User> {
        // Валидация параметров
        if (username.isBlank()) {
            return Result.failure(ValidationException("Username cannot be empty"))
        }
        
        // Дополнительная валидация
        if (username.length > 50) {
            return Result.failure(ValidationException("Username cannot exceed 50 characters"))
        }
        
        if (role.isBlank()) {
            return Result.failure(ValidationException("Role cannot be empty"))
        }
        
        return projectRepository.inviteEmployee(projectId, username, role)
    }
}