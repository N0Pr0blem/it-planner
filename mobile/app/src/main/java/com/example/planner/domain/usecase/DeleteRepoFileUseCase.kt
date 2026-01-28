package com.example.planner.domain.usecase

import com.example.planner.domain.repository.RepoRepository
import javax.inject.Inject

class DeleteRepoFileUseCase @Inject constructor(
    private val repoRepository: RepoRepository
) {
    suspend operator fun invoke(projectId: Long, fileId: Long) =
        repoRepository.deleteRepoFile(projectId, fileId)
}
