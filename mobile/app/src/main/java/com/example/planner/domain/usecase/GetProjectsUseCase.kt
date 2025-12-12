package com.example.planner.domain.usecase

import com.example.planner.domain.model.Project
import com.example.planner.domain.repository.ProjectRepository

class GetProjectsUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(): Result<List<Project>> {
        return projectRepository.getProjects()
    }
}