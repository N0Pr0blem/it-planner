package com.example.planner.domain.usecase

import com.example.planner.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskTrackingUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long) = taskRepository.getTracking(taskId)
}
