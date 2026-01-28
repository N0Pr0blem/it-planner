package com.example.planner.domain.usecase

import com.example.planner.domain.repository.ProjectRepository

class DeleteProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long): Result<Unit> {
        return projectRepository.deleteProject(projectId)
    }
}
