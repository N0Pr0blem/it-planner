package com.example.planner.domain.usecase

import com.example.planner.domain.model.Task
import com.example.planner.domain.repository.ProjectRepository

class GetTaskUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long, taskId: Long): Result<Task> {
        return projectRepository.getTask(projectId, taskId)
    }
}