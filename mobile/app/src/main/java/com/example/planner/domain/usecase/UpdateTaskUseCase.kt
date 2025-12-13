package com.example.planner.domain.usecase

import com.example.planner.domain.exception.ValidationException
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
        // Валидация параметров задачи
        if (name != null && name.isBlank()) {
            return Result.failure(ValidationException("Task name cannot be empty"))
        }
        
        // Дополнительная валидация
        if (name != null && name.length > 200) {
            return Result.failure(ValidationException("Task name cannot exceed 200 characters"))
        }
        
        if (description != null && description.length > 2000) {
            return Result.failure(ValidationException("Task description cannot exceed 2000 characters"))
        }
        
        return projectRepository.updateTask(projectId, taskId, name, urgency, complexity, status, description)
    }
}