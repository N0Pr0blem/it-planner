package com.example.planner.domain.usecase

import com.example.planner.domain.exception.ValidationException
import com.example.planner.domain.model.ProjectMember
import com.example.planner.domain.model.ProjectRole
import com.example.planner.domain.repository.ProjectRepository

class InviteEmployeeUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long, username: String, role: ProjectRole): Result<ProjectMember> {
        if (username.isBlank()) {
            return Result.failure(ValidationException("Username cannot be empty"))
        }

        if (username.length > 50) {
            return Result.failure(ValidationException("Username cannot exceed 50 characters"))
        }

        return projectRepository.inviteEmployee(projectId, username, role)
    }
}
