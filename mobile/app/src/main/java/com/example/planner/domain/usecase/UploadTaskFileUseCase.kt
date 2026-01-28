package com.example.planner.domain.usecase

import com.example.planner.domain.repository.TaskRepository
import java.io.File
import javax.inject.Inject

class UploadTaskFileUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long, file: File, displayName: String?, contentType: String?) =
        taskRepository.uploadTaskFile(taskId, file, displayName, contentType)
}
