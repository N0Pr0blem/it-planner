package com.example.planner.domain.usecase

import com.example.planner.domain.repository.TaskRepository
import javax.inject.Inject

class AssignTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(projectId: Long, taskId: Long, employeeId: Long) =
        taskRepository.assignTask(projectId, taskId, employeeId)
}
