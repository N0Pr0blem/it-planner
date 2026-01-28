package com.example.planner.domain.usecase

import com.example.planner.domain.exception.ValidationException
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.TaskComplexity
import com.example.planner.domain.model.TaskUrgency
import com.example.planner.domain.repository.ProjectRepository

class CreateTaskUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(
        projectId: Long,
        name: String,
        description: String,
        urgency: TaskUrgency,
        complexity: TaskComplexity
    ): Result<Task> {
        // Валидация параметров задачи
        if (name.isBlank()) {
            return Result.failure(ValidationException("Task name cannot be empty"))
        }
        
        // Дополнительная валидация
        if (name.length > 200) {
            return Result.failure(ValidationException("Task name cannot exceed 200 characters"))
        }
        
        if (description.length > 2000) {
            return Result.failure(ValidationException("Task description cannot exceed 2000 characters"))
        }
        
        return projectRepository.createTask(projectId, name, description, urgency, complexity)
    }
}
