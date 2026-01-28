package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.domain.model.TaskComplexity
import com.example.planner.domain.model.TaskUrgency
import com.example.planner.domain.usecase.CreateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class CreateTaskUiState(
    val isLoading: Boolean = false,
    val taskCreated: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {

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
            val createResult = withContext(Dispatchers.IO) {
                createTaskUseCase(
                    projectId,
                    title,
                    description,
                    priority,
                    complexity
                )
            }
            createResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, taskCreated = true)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to create task: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
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
