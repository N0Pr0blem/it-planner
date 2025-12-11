package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.repository.ProjectRepository
import com.example.planner.ui.screens.ProjectUi
import com.example.planner.data.mapper.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProjectsUiState(
    val isLoading: Boolean = false,
    val projects: List<ProjectUi> = emptyList(),
    val error: String? = null,
    val projectCreated: Boolean = false
)

class ProjectsViewModel : ViewModel() {
    private val repository = ProjectRepository()

    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getProjects()
                .onSuccess { projects ->
                    val projectsUi = projects.toUi()
                    _uiState.value = _uiState.value.copy(isLoading = false, projects = projectsUi)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to load projects")
                }
        }
    }

    fun createProject(name: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.createProject(name)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, projectCreated = true)
                    loadProjects()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.deleteProject(projectId)
                .onSuccess {
                    loadProjects()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
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
