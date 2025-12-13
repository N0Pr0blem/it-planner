package com.example.planner.domain.usecase

import com.example.planner.domain.exception.ValidationException
import com.example.planner.domain.model.Project
import com.example.planner.domain.repository.ProjectRepository

class CreateProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(name: String): Result<Project> {
        // Валидация имени проекта
        if (name.isBlank()) {
            return Result.failure(ValidationException("Project name cannot be empty"))
        }
        
        // Дополнительная валидация
        if (name.length > 100) {
            return Result.failure(ValidationException("Project name cannot exceed 100 characters"))
        }
        
        return projectRepository.createProject(name)
    }
}