package com.example.planner.domain.usecase

import com.example.planner.domain.repository.RepoRepository
import java.io.File
import javax.inject.Inject

class UploadRepoFileUseCase @Inject constructor(
    private val repoRepository: RepoRepository
) {
    suspend operator fun invoke(projectId: Long, file: File, filename: String?, contentType: String?) =
        repoRepository.uploadRepoFile(projectId, file, filename, contentType)
}
