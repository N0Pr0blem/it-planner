package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskUrgency
import com.example.planner.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateTaskUiState(
    val isLoading: Boolean = false,
    val taskCreated: Boolean = false,
    val error: String? = null
)

class CreateTaskViewModel : ViewModel() {
    private val repository = TaskRepository()

    private val _uiState = MutableStateFlow(CreateTaskUiState())
    val uiState: StateFlow<CreateTaskUiState> = _uiState.asStateFlow()

    fun createTask(
        projectId: Long,
        title: String,
        description: String,
        priority: TaskUrgency,
        complexity: TaskComplexity
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.createTask(projectId, title, priority, complexity, description)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, taskCreated = true)
                }
                .onFailure { e ->\n                    val errorMessage = when (e) {\n                        is com.example.planner.domain.exception.ValidationException -> e.message\n                        is com.example.planner.domain.exception.NetworkException -> "Network error: \${e.message}"\n                        else -> "Failed to create task: \${e.message}"\n                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetTaskCreated() {
        _uiState.value = CreateTaskUiState()
    }
}
