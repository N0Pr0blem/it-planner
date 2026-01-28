package com.example.planner.domain.usecase

import com.example.planner.domain.repository.TaskRepository
import javax.inject.Inject

class DownloadTaskFileUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long, fileId: Long) =
        taskRepository.downloadTaskFile(taskId, fileId)
}
