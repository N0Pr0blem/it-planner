package com.example.planner.domain.usecase

import com.example.planner.domain.model.Task
import com.example.planner.domain.repository.ProjectRepository

class UpdateTaskUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(
        projectId: Long,
        taskId: Long,
        name: String? = null,
        urgency: String? = null,
        complexity: String? = null,
        status: String? = null,
        description: String? = null
    ): Result<Task> {
        // Можно добавить валидацию параметров задачи
        if (name != null && name.isBlank()) {
            return Result.failure(IllegalArgumentException("Task name cannot be empty"))
        }
        
        return projectRepository.updateTask(projectId, taskId, name, urgency, complexity, status, description)
    }
}