package com.example.planner.domain.usecase

import com.example.planner.domain.repository.ProjectRepository

class DeleteEmployeeUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Long, employeeId: Long): Result<Unit> {
        return projectRepository.deleteEmployee(projectId, employeeId)
    }
}