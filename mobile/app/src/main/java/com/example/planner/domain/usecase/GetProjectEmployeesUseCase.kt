package com.example.planner.domain.usecase

import com.example.planner.domain.model.User
import com.example.planner.domain.repository.ProjectRepository

class GetProjectEmployeesUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long): Result<List<User>> {
        return projectRepository.getProjectEmployees(projectId)
    }
}