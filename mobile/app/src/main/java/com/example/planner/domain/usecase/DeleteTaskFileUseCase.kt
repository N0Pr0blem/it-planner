package com.example.planner.domain.usecase

import com.example.planner.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskFileUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long, fileId: Long) =
        taskRepository.deleteTaskFile(taskId, fileId)
}
