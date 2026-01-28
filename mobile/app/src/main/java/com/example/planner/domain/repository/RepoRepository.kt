package com.example.planner.domain.repository

import com.example.planner.domain.model.RepoFile
import com.example.planner.domain.model.SimpleMessage
import java.io.File

interface RepoRepository {
    suspend fun getRepoFiles(projectId: Long): Result<List<RepoFile>>
    suspend fun uploadRepoFile(projectId: Long, file: File, filename: String?, contentType: String?): Result<String>
    suspend fun downloadRepoFile(projectId: Long, fileId: Long): Result<ByteArray>
    suspend fun deleteRepoFile(projectId: Long, fileId: Long): Result<Unit>
}
