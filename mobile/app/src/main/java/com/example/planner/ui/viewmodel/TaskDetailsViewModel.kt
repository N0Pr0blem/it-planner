package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.domain.model.TaskComplexity
import com.example.planner.domain.model.TaskStatus
import com.example.planner.domain.model.TaskUrgency
import com.example.planner.domain.model.TrackingRecord
import com.example.planner.domain.usecase.AssignTaskUseCase
import com.example.planner.domain.usecase.CreateTrackingUseCase
import com.example.planner.domain.usecase.DeleteTaskFileUseCase
import com.example.planner.domain.usecase.DeleteTrackingUseCase
import com.example.planner.domain.usecase.DownloadTaskFileUseCase
import com.example.planner.domain.usecase.GetProjectEmployeesUseCase
import com.example.planner.domain.usecase.GetTaskDetailsUseCase
import com.example.planner.domain.usecase.GetTaskFilesUseCase
import com.example.planner.domain.usecase.GetTaskTrackingUseCase
import com.example.planner.domain.usecase.GetTaskUseCase
import com.example.planner.domain.usecase.UpdateTaskUseCase
import com.example.planner.domain.usecase.UploadTaskFileUseCase
import com.example.planner.ui.screens.TaskFileUi
import com.example.planner.ui.mapper.toUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class TaskDetailsUiState(
    val isLoading: Boolean = false,
    val taskTitle: String = "",
    val status: TaskStatus = TaskStatus.TO_DO,
    val priority: TaskUrgency = TaskUrgency.MEDIUM,
    val complexity: TaskComplexity = TaskComplexity.MEDIUM,
    val description: String = "",
    val files: List<TaskFileUi> = emptyList(),
    val totalHours: Double = 0.0,
    val trackingRecords: List<TrackingRecord> = emptyList(),
    val createdBy: String = "",
    val assignedTo: String = "",
    val availableAssignees: List<com.example.planner.ui.screens.AssigneeUi> = emptyList(),
    val error: String? = null,
    val taskUpdated: Boolean = false
)

sealed interface TaskFileEvent {
    data class Downloaded(val filename: String, val bytes: ByteArray) : TaskFileEvent
    data class Error(val message: String) : TaskFileEvent
}

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val getTaskUseCase: GetTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getProjectEmployeesUseCase: GetProjectEmployeesUseCase,
    private val getTaskDetailsUseCase: GetTaskDetailsUseCase,
    private val getTaskFilesUseCase: GetTaskFilesUseCase,
    private val uploadTaskFileUseCase: UploadTaskFileUseCase,
    private val downloadTaskFileUseCase: DownloadTaskFileUseCase,
    private val deleteTaskFileUseCase: DeleteTaskFileUseCase,
    private val getTaskTrackingUseCase: GetTaskTrackingUseCase,
    private val createTrackingUseCase: CreateTrackingUseCase,
    private val deleteTrackingUseCase: DeleteTrackingUseCase,
    private val assignTaskUseCase: AssignTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailsUiState())
    val uiState: StateFlow<TaskDetailsUiState> = _uiState.asStateFlow()

    private val _fileEvents = MutableSharedFlow<TaskFileEvent>()
    val fileEvents: SharedFlow<TaskFileEvent> = _fileEvents.asSharedFlow()

    private var currentProjectId: Long = 0
    private var currentTaskId: Long = 0

    fun loadTaskDetails(projectId: Long, taskId: Long) {
        currentProjectId = projectId
        currentTaskId = taskId

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val taskResult = withContext(Dispatchers.IO) {
                getTaskUseCase(projectId, taskId)
            }
            taskResult
                .onSuccess { task ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        taskTitle = task.name,
                        status = task.status,
                        priority = task.urgency,
                        complexity = task.complexity,
                        createdBy = task.assignedBy?.let { "${it.firstName ?: ""} ${it.secondName ?: ""}" } ?: "",
                        assignedTo = task.assignedTo?.let { "${it.firstName ?: ""} ${it.secondName ?: ""}" } ?: ""
                    )
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to load task: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage ?: "Failed to load task")
                }

            val detailsResult = withContext(Dispatchers.IO) { getTaskDetailsUseCase(taskId) }
            detailsResult
                .onSuccess { details ->
                    _uiState.value = _uiState.value.copy(
                        description = details.message,
                    )
                }

            loadTracking()
            loadTaskFiles()
            loadAssignees(projectId)
        }
    }

    fun loadTracking(taskId: Long = currentTaskId) {
        viewModelScope.launch {
            val trackingResult = withContext(Dispatchers.IO) { getTaskTrackingUseCase(taskId) }
            trackingResult
                .onSuccess { tracking ->
                    _uiState.value = _uiState.value.copy(
                        totalHours = tracking.totalHours,
                        trackingRecords = tracking.records
                    )
                }
        }
    }

    fun loadTaskFiles(taskId: Long = currentTaskId) {
        viewModelScope.launch {
            val filesResult = withContext(Dispatchers.IO) { getTaskFilesUseCase(taskId) }
            filesResult
                .onSuccess { files ->
                    val uiFiles = files.map { it.toUi() }
                    _uiState.value = _uiState.value.copy(files = uiFiles)
                }
                .onFailure { e ->
                    _fileEvents.emit(TaskFileEvent.Error(e.message ?: "Failed to load files"))
                }
        }
    }

    fun uploadTaskFile(file: File, displayName: String?, contentType: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val uploadResult = withContext(Dispatchers.IO) {
                uploadTaskFileUseCase(currentTaskId, file, displayName, contentType)
            }
            uploadResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    loadTaskFiles()
                }
                .onFailure { e ->
                    val message = when {
                        e.message?.contains("HTTP 403") == true -> "No permission to upload file."
                        else -> e.message ?: "Failed to upload file"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _fileEvents.emit(TaskFileEvent.Error(message))
                }
        }
    }

    fun downloadTaskFile(fileId: Long, filename: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val downloadResult = withContext(Dispatchers.IO) {
                downloadTaskFileUseCase(currentTaskId, fileId)
            }
            downloadResult
                .onSuccess { bytes ->
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _fileEvents.emit(TaskFileEvent.Downloaded(filename, bytes))
                }
                .onFailure { e ->
                    val message = when {
                        e.message?.contains("HTTP 403") == true -> "No permission to download file."
                        else -> e.message ?: "Failed to download file"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _fileEvents.emit(TaskFileEvent.Error(message))
                }
        }
    }

    fun deleteTaskFile(fileId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val deleteResult = withContext(Dispatchers.IO) {
                deleteTaskFileUseCase(currentTaskId, fileId)
            }
            deleteResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    loadTaskFiles()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _fileEvents.emit(TaskFileEvent.Error(e.message ?: "Failed to delete file"))
                }
        }
    }

    fun assignResponsible(employee: com.example.planner.ui.screens.AssigneeUi) {
        val employeeId = employee.id.toLongOrNull() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val assignResult = withContext(Dispatchers.IO) {
                assignTaskUseCase(currentProjectId, currentTaskId, employeeId)
            }
            assignResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        assignedTo = employee.name,
                        taskUpdated = true
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to assign task"
                    )
                }
        }
    }

    private fun loadAssignees(projectId: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getProjectEmployeesUseCase(projectId)
            }
            result.onSuccess { employees ->
                val assignees = employees.map { member ->
                    val name = listOfNotNull(member.firstName, member.secondName)
                        .joinToString(" ")
                        .trim()
                        .ifBlank { "User ${member.id}" }
                    com.example.planner.ui.screens.AssigneeUi(
                        id = member.id.toString(),
                        name = name
                    )
                }
                _uiState.value = _uiState.value.copy(availableAssignees = assignees)
            }
        }
    }

    fun updateStatus(status: TaskStatus) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val updateResult = withContext(Dispatchers.IO) {
                updateTaskUseCase(currentProjectId, currentTaskId, status = status.name)
            }
            updateResult
                .onSuccess { task ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        status = task.status,
                        priority = task.urgency,
                        complexity = task.complexity,
                        taskUpdated = true
                    )
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to load task: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun updateTask(
        title: String,
        description: String,
        priority: TaskUrgency,
        complexity: TaskComplexity
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val updateResult = withContext(Dispatchers.IO) {
                updateTaskUseCase(
                    currentProjectId,
                    currentTaskId,
                    name = title,
                    urgency = priority,
                    complexity = complexity,
                    description = description
                )
            }
            updateResult
                .onSuccess { task ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        taskTitle = task.name,
                        status = task.status,
                        priority = task.urgency,
                        complexity = task.complexity,
                        description = description,
                        taskUpdated = true
                    )
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to update task: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun addTracking(hours: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val createResult = withContext(Dispatchers.IO) {
                createTrackingUseCase(currentProjectId, currentTaskId, hours)
            }
            createResult
                .onSuccess {
                    loadTracking()
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to add tracking: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun deleteTracking(trackingId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val deleteResult = withContext(Dispatchers.IO) {
                deleteTrackingUseCase(trackingId)
            }
            deleteResult
                .onSuccess {
                    loadTracking()
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to load task: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetTaskUpdated() {
        _uiState.value = _uiState.value.copy(taskUpdated = false)
    }

}
