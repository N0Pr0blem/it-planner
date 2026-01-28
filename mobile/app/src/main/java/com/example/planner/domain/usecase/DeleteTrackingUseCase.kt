package com.example.planner.domain.usecase

import com.example.planner.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTrackingUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(trackingId: Long) = taskRepository.deleteTracking(trackingId)
}
