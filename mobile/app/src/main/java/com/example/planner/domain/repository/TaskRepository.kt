package com.example.planner.domain.repository

import com.example.planner.domain.model.SimpleMessage
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.TaskFile
import com.example.planner.domain.model.TaskStatus
import com.example.planner.domain.model.TrackingRecord
import com.example.planner.domain.model.TrackingSummary
import java.io.File

interface TaskRepository {
    suspend fun getTask(taskId: Long): Result<Task>
    suspend fun getTaskDetails(taskId: Long): Result<SimpleMessage>
    suspend fun getTaskFiles(taskId: Long): Result<List<TaskFile>>
    suspend fun uploadTaskFile(taskId: Long, file: File, displayName: String?, contentType: String?): Result<SimpleMessage>
    suspend fun downloadTaskFile(taskId: Long, fileId: Long): Result<ByteArray>
    suspend fun deleteTaskFile(taskId: Long, fileId: Long): Result<SimpleMessage>
    suspend fun assignTask(projectId: Long, taskId: Long, employeeId: Long): Result<String>
    suspend fun getTracking(taskId: Long): Result<TrackingSummary>
    suspend fun createTracking(
        projectId: Long,
        taskId: Long,
        hours: Double,
        date: java.time.LocalDate = java.time.LocalDate.now()
    ): Result<TrackingRecord>
    suspend fun deleteTracking(trackingId: Long): Result<SimpleMessage>
    suspend fun updateStatus(projectId: Long, taskId: Long, status: TaskStatus): Result<Task>
    suspend fun updateTask(
        projectId: Long,
        taskId: Long,
        name: String? = null,
        description: String? = null,
        urgency: com.example.planner.domain.model.TaskUrgency? = null,
        complexity: com.example.planner.domain.model.TaskComplexity? = null
    ): Result<Task>
}
