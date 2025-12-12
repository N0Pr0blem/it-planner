package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.dto.task.TaskInfoResponseDto
import com.example.planner.data.dto.tracking.TrackingResponseDto
import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.data.model.task.TaskUrgency
import com.example.planner.data.repository.TaskRepository
import com.example.planner.ui.screens.TaskFileUi
import com.example.planner.data.mapper.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TaskDetailsUiState(
    val isLoading: Boolean = false,
    val taskTitle: String = "",
    val status: TaskStatus = TaskStatus.TO_DO,
    val priority: TaskUrgency = TaskUrgency.MEDIUM,
    val complexity: TaskComplexity = TaskComplexity.MEDIUM,
    val description: String = "",
    val files: List<TaskFileUi> = emptyList(),
    val totalHours: Double = 0.0,
    val trackingRecords: List<TrackingResponseDto> = emptyList(),
    val createdBy: String = "",
    val assignedTo: String = "",
    val error: String? = null,
    val taskUpdated: Boolean = false
)

class TaskDetailsViewModel : ViewModel() {
    private val repository = TaskRepository()

    private val _uiState = MutableStateFlow(TaskDetailsUiState())
    val uiState: StateFlow<TaskDetailsUiState> = _uiState.asStateFlow()

    private var currentProjectId: Long = 0
    private var currentTaskId: Long = 0

    fun loadTaskDetails(projectId: Long, taskId: Long) {
        currentProjectId = projectId
        currentTaskId = taskId

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getTask(projectId, taskId)
                .onSuccess { task ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        taskTitle = task.name,
                        status = TaskStatus.valueOf(task.status),
                        priority = TaskUrgency.valueOf(task.urgency),
                        complexity = TaskComplexity.valueOf(task.complexity),
                        createdBy = task.assignedBy?.let { "${it.firstName ?: ""} ${it.secondName ?: ""}" } ?: "",
                        assignedTo = task.assignedTo?.let { "${it.firstName ?: ""} ${it.secondName ?: ""}" } ?: ""
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to load task")
                }

            repository.getTaskDetails(projectId, taskId)
                .onSuccess { details ->
                    _uiState.value = _uiState.value.copy(
                        description = details.descriptionFile ?: "",
                        files = emptyList() // TODO: получить файлы задачи
                    )
                }

            loadTracking()
        }
    }

    fun loadTracking(projectId: Long = currentProjectId, taskId: Long = currentTaskId) {
        viewModelScope.launch {
            repository.getTaskTracking(projectId, taskId)
                .onSuccess { tracking ->
                    _uiState.value = _uiState.value.copy(
                        totalHours = tracking.hourSum,
                        trackingRecords = tracking.trekkingList
                    )
                }
        }
    }

    fun updateStatus(status: TaskStatus) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.updateTask(currentProjectId, currentTaskId, status = status)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        status = status,
                        taskUpdated = true
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun addTracking(hours: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.createTracking(currentProjectId, currentTaskId, hours)
                .onSuccess {
                    loadTracking()
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun deleteTracking(trackingId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.deleteTracking(currentProjectId, currentTaskId, trackingId)
                .onSuccess {
                    loadTracking()
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
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
