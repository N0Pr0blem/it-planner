package com.example.planner.domain.usecase

import com.example.planner.domain.model.Task
import com.example.planner.domain.repository.ProjectRepository

class CreateTaskUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(
        projectId: Long,
        name: String,
        description: String,
        urgency: String,
        complexity: String
    ): Result<Task> {
        // Можно добавить валидацию параметров задачи
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Task name cannot be empty"))
        }
        
        return projectRepository.createTask(projectId, name, description, urgency, complexity)
    }
}