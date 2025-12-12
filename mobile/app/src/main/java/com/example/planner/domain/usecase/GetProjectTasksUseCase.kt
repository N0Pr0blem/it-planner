package com.example.planner.domain.usecase

import com.example.planner.domain.model.Task
import com.example.planner.domain.repository.ProjectRepository

class GetProjectTasksUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long): Result<List<Task>> {
        return projectRepository.getProjectTasks(projectId)
    }
}