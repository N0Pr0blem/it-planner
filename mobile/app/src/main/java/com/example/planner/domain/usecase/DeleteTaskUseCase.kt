package com.example.planner.domain.usecase

import com.example.planner.domain.repository.ProjectRepository

class DeleteTaskUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long, taskId: Long): Result<Unit> {
        return projectRepository.deleteTask(projectId, taskId)
    }
}