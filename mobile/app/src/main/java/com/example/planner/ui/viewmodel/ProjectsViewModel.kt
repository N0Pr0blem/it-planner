package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.domain.usecase.CreateProjectUseCase
import com.example.planner.domain.usecase.DeleteProjectUseCase
import com.example.planner.domain.usecase.GetProjectsUseCase
import com.example.planner.ui.screens.ProjectUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class ProjectsUiState(
    val isLoading: Boolean = false,
    val projects: List<ProjectUi> = emptyList(),
    val error: String? = null,
    val projectCreated: Boolean = false
)

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val projectsResult = withContext(Dispatchers.IO) {
                getProjectsUseCase()
            }
            projectsResult
                .onSuccess { projects ->
                    val projectsUi = withContext(Dispatchers.Default) {
                        projects.map { project ->
                            ProjectUi(
                                id = project.id.toString(),
                                name = project.name,
                                date = ""
                            )
                        }
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, projects = projectsUi)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to load projects: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun createProject(name: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val createResult = withContext(Dispatchers.IO) {
                createProjectUseCase(name)
            }
            createResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, projectCreated = true)
                    loadProjects()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to create project")
                }
        }
    }

    fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val deleteResult = withContext(Dispatchers.IO) {
                deleteProjectUseCase(projectId)
            }
            deleteResult
                .onSuccess {
                    loadProjects()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to delete project")
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetProjectCreated() {
        _uiState.value = _uiState.value.copy(projectCreated = false)
    }

}
