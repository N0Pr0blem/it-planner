package com.example.planner.domain.usecase

import com.example.planner.domain.model.Project
import com.example.planner.domain.repository.ProjectRepository

class CreateProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(name: String): Result<Project> {
        // Можно добавить валидацию имени проекта
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Project name cannot be empty"))
        }
        
        return projectRepository.createProject(name)
    }
}