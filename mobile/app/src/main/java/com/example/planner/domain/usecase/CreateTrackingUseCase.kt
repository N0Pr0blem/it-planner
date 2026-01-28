package com.example.planner.domain.usecase

import com.example.planner.domain.repository.TaskRepository
import java.time.LocalDate
import javax.inject.Inject

class CreateTrackingUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(projectId: Long, taskId: Long, hours: Double, date: LocalDate = LocalDate.now()) =
        taskRepository.createTracking(projectId, taskId, hours, date)
}
