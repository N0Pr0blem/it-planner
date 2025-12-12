package com.example.planner.domain.usecase

import com.example.planner.domain.model.User
import com.example.planner.domain.repository.ProjectRepository

class InviteEmployeeUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long, username: String, role: String): Result<User> {
        // Можно добавить валидацию параметров
        if (username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username cannot be empty"))
        }
        
        return projectRepository.inviteEmployee(projectId, username, role)
    }
}