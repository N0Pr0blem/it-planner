package com.example.planner.domain.usecase

import com.example.planner.domain.repository.ProjectRepository
import javax.inject.Inject

class GetProjectUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(projectId: Long) = projectRepository.getProject(projectId)
}
