package com.example.planner.data.repository

import com.example.planner.data.dto.MessageResponseDto
import com.example.planner.data.dto.task.CreateTaskInfoRequestDto
import com.example.planner.data.dto.task.TaskInfoListing
import com.example.planner.data.dto.task.TaskInfoResponseDto
import com.example.planner.data.dto.task.TaskFileDto
import com.example.planner.data.dto.task.UpdateTaskInfoRequestDto
import com.example.planner.data.dto.tracking.AllTrackingResponse
import com.example.planner.data.dto.tracking.TrackingCreationDto
import com.example.planner.data.dto.tracking.TrackingResponseDto
import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.data.model.task.TaskUrgency
import com.example.planner.data.network.ApiService
import com.example.planner.data.mapper.toDomain
import com.example.planner.domain.model.SimpleMessage
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.TaskComplexity as DomainComplexity
import com.example.planner.domain.model.TaskFile
import com.example.planner.domain.model.TaskStatus as DomainStatus
import com.example.planner.domain.model.TaskUrgency as DomainUrgency
import com.example.planner.domain.model.TrackingRecord
import com.example.planner.domain.model.TrackingSummary
import com.example.planner.domain.repository.TaskRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.time.LocalDate
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val api: ApiService
) : TaskRepository {

    private fun TaskInfoResponseDto.toDomainTask(): Task = toDomain()

    private fun toDataStatus(status: DomainStatus): TaskStatus = when (status) {
        DomainStatus.TO_DO -> TaskStatus.TO_DO
        DomainStatus.IN_PROGRESS -> TaskStatus.IN_PROGRESS
        DomainStatus.REVIEW -> TaskStatus.REVIEW
        DomainStatus.IN_TEST -> TaskStatus.IN_TEST
        DomainStatus.DONE -> TaskStatus.DONE
    }

    private fun toDataUrgency(urgency: DomainUrgency): TaskUrgency = when (urgency) {
        DomainUrgency.URGENT -> TaskUrgency.URGENT
        DomainUrgency.MEDIUM -> TaskUrgency.MEDIUM
        DomainUrgency.NOT_URGENT -> TaskUrgency.NOT_URGENT
    }

    private fun toDataComplexity(complexity: DomainComplexity): TaskComplexity = when (complexity) {
        DomainComplexity.HARD -> TaskComplexity.HARD
        DomainComplexity.MEDIUM -> TaskComplexity.MEDIUM
        DomainComplexity.EASY -> TaskComplexity.EASY
    }

    override suspend fun getTask(taskId: Long): Result<Task> {
        return try {
            val response = api.getTask(taskId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun createTaskInternal(
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
            val response = api.createTask(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = when (response.code()) {
                    403 -> "Access denied: You don't have permission to create tasks in this project"
                    400 -> errorBody ?: "Invalid request data"
                    else -> errorBody ?: "Failed to create task (HTTP ${response.code()})"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun updateTaskInternal(
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
            val response = api.updateTask(taskId, request)
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

    override suspend fun deleteTask(taskId: Long): Result<Unit> {
        return try {
            val response = api.deleteTask(taskId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTaskDetails(taskId: Long): Result<SimpleMessage> {
        return try {
            val response = api.getTaskDetails(taskId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load task details"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTaskFiles(taskId: Long): Result<List<TaskFile>> {
        return try {
            val response = api.getTaskFiles(taskId)
            if (response.isSuccessful) {
                val files = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(files)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load task files"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadTaskFile(
        taskId: Long,
        file: File,
        displayName: String?,
        contentType: String?
    ): Result<SimpleMessage> {
        return try {
            val mediaType = contentType?.toMediaTypeOrNull()
            val requestBody = file.asRequestBody(mediaType)
            val safeName = displayName?.takeIf { it.isNotBlank() } ?: file.name
            val part = MultipartBody.Part.createFormData("file", safeName, requestBody)
            val response = api.uploadTaskFile(taskId, part)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (!errorBody.isNullOrBlank()) {
                    errorBody
                } else {
                    "HTTP ${response.code()}: Failed to upload file"
                }
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downloadTaskFile(taskId: Long, fileId: Long): Result<ByteArray> {
        return try {
            val response = api.downloadTaskFile(taskId, fileId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.bytes()) }
                    ?: Result.failure(Exception("Empty file"))
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (!errorBody.isNullOrBlank()) {
                    errorBody
                } else {
                    "HTTP ${response.code()}: Failed to download file"
                }
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTaskFile(taskId: Long, fileId: Long): Result<SimpleMessage> {
        return try {
            val response = api.deleteTaskFile(taskId, fileId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete file"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun assignTask(projectId: Long, taskId: Long, employeeId: Long): Result<String> {
        return try {
            val response = api.assignTaskToEmployee(projectId, taskId, employeeId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "Assigned")
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to assign task"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTracking(taskId: Long): Result<TrackingSummary> {
        return try {
            val response = api.getTaskTracking(taskId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load tracking"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTracking(
        projectId: Long,
        taskId: Long,
        hours: Double,
        date: LocalDate
    ): Result<TrackingRecord> {
        return try {
            val request = TrackingCreationDto(
                date = date,
                hours = hours,
                projectId = projectId,
                taskId = taskId
            )
            val response = api.createTracking(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create tracking"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTracking(trackingId: Long): Result<SimpleMessage> {
        return try {
            val response = api.deleteTracking(trackingId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete tracking"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatus(
        projectId: Long,
        taskId: Long,
        status: DomainStatus
    ): Result<Task> {
        val dataStatus = toDataStatus(status)
        return updateTaskInternal(taskId, status = dataStatus).mapCatching { it.toDomain() }
    }

    override suspend fun updateTask(
        projectId: Long,
        taskId: Long,
        name: String?,
        description: String?,
        urgency: DomainUrgency?,
        complexity: DomainComplexity?
    ): Result<Task> {
        val dataUrgency = urgency?.let { toDataUrgency(it) }
        val dataComplexity = complexity?.let { toDataComplexity(it) }
        return updateTaskInternal(taskId, name, dataUrgency, dataComplexity, null, description)
            .mapCatching { it.toDomain() }
    }
}
