package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.di.AppModule
import com.example.planner.domain.model.Project
import com.example.planner.ui.screens.ProjectUi
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
    private val getProjectsUseCase = AppModule.getProjectsUseCase
    private val createProjectUseCase = AppModule.createProjectUseCase

    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getProjectsUseCase()
                .onSuccess { projects ->
                    // Преобразуем доменные модели в UI модели
                    val projectsUi = projects.map { project ->
                        ProjectUi(
                            id = project.id.toString(),
                            name = project.name,
                            date = "" // TODO: добавить форматирование даты
                        )
                    }
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
            createProjectUseCase(name)
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
            // TODO: добавить use case для удаления проекта
            // Пока используем старый репозиторий
            val legacyRepository = com.example.planner.data.repository.ProjectRepositoryLegacy()
            legacyRepository.deleteProject(projectId)
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
