package com.example.planner.data.repository

import com.example.planner.data.dto.task.CreateTaskInfoRequestDto
import com.example.planner.data.dto.task.TaskDetailsInfo
import com.example.planner.data.dto.task.TaskInfoListing
import com.example.planner.data.dto.task.TaskInfoResponseDto
import com.example.planner.data.dto.task.UpdateTaskInfoRequestDto
import com.example.planner.data.dto.tracking.AllTrackingResponse
import com.example.planner.data.dto.tracking.TrackingCreationDto
import com.example.planner.data.dto.tracking.TrackingResponseDto
import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.data.model.task.TaskUrgency
import com.example.planner.data.network.RetrofitInstance
import java.time.LocalDate

class TaskRepository {
    private val api = RetrofitInstance.api

    suspend fun getProjectTasks(projectId: Long): Result<List<TaskInfoListing>> {
        return try {
            val response = api.getProjectTasks(projectId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load tasks"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTask(projectId: Long, taskId: Long): Result<TaskInfoResponseDto> {
        return try {
            val response = api.getTask(projectId, taskId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTask(
        projectId: Long,
        name: String,
        urgency: TaskUrgency,
        complexity: TaskComplexity,
        description: String
    ): Result<TaskInfoResponseDto> {
        return try {
            val request = CreateTaskInfoRequestDto(
                name = name,
                urgency = urgency,
                complexity = complexity,
                projectId = projectId,
                description = description
            )
            val response = api.createTask(projectId, request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTask(
        projectId: Long,
        taskId: Long,
        name: String? = null,
        urgency: TaskUrgency? = null,
        complexity: TaskComplexity? = null,
        status: TaskStatus? = null,
        description: String? = null
    ): Result<TaskInfoResponseDto> {
        return try {
            val request = UpdateTaskInfoRequestDto(
                name = name,
                urgency = urgency,
                complexity = complexity,
                status = status,
                description = description
            )
            val response = api.updateTask(projectId, taskId, request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to update task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTask(projectId: Long, taskId: Long): Result<Unit> {
        return try {
            val response = api.deleteTask(projectId, taskId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTaskDetails(projectId: Long, taskId: Long): Result<TaskDetailsInfo> {
        return try {
            val response = api.getTaskDetails(projectId, taskId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load task details"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Tracking
    suspend fun getTaskTracking(projectId: Long, taskId: Long): Result<AllTrackingResponse> {
        return try {
            val response = api.getTaskTracking(projectId, taskId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load tracking"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTracking(
        projectId: Long,
        taskId: Long,
        hours: Double,
        date: LocalDate = LocalDate.now()
    ): Result<TrackingResponseDto> {
        return try {
            val request = TrackingCreationDto(
                date = date,
                hours = hours,
                projectId = projectId,
                taskId = taskId
            )
            val response = api.createTracking(projectId, taskId, request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create tracking"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTracking(projectId: Long, taskId: Long, trackingId: Long): Result<Unit> {
        return try {
            val response = api.deleteTracking(projectId, taskId, trackingId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete tracking"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
