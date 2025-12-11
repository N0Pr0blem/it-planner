package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.dto.employee.EmployeeResponseDto
import com.example.planner.data.dto.repo.ProjectRepoFileDto
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.data.model.user.ProjectRole
import com.example.planner.data.repository.ProjectRepository
import com.example.planner.data.repository.TaskRepository
import com.example.planner.ui.screens.ProjectTaskUi
import com.example.planner.data.mapper.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProjectDetailsUiState(
    val isLoading: Boolean = false,
    val projectName: String = "",
    val tasks: List<ProjectTaskUi> = emptyList(),
    val employees: List<EmployeeResponseDto> = emptyList(),
    val repoFiles: List<ProjectRepoFileDto> = emptyList(),
    val error: String? = null,
    val taskCreated: Boolean = false,
    val employeeInvited: Boolean = false
)

class ProjectDetailsViewModel : ViewModel() {
    private val projectRepository = ProjectRepository()
    private val taskRepository = TaskRepository()

    private val _uiState = MutableStateFlow(ProjectDetailsUiState())
    val uiState: StateFlow<ProjectDetailsUiState> = _uiState.asStateFlow()

    private var currentProjectId: Long = 0

    fun loadProjectDetails(projectId: Long) {
        currentProjectId = projectId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            projectRepository.getProject(projectId)
                .onSuccess { project ->
                    _uiState.value = _uiState.value.copy(projectName = project.name)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
        }
        loadTasks(projectId)
    }

    fun loadTasks(projectId: Long = currentProjectId) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            taskRepository.getProjectTasks(projectId)
                .onSuccess { tasks ->
                    val tasksUi = tasks.toUi()
                    _uiState.value = _uiState.value.copy(isLoading = false, tasks = tasksUi)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to load tasks")
                }
        }
    }

    fun loadEmployees(projectId: Long = currentProjectId) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            projectRepository.getEmployees(projectId)
                .onSuccess { employees ->
                    _uiState.value = _uiState.value.copy(isLoading = false, employees = employees)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun loadRepoFiles(projectId: Long = currentProjectId) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            projectRepository.getRepoFiles(projectId)
                .onSuccess { files ->
                    _uiState.value = _uiState.value.copy(isLoading = false, repoFiles = files)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun inviteEmployee(username: String, role: ProjectRole) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            projectRepository.inviteEmployee(currentProjectId, username, role)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, employeeInvited = true)
                    loadEmployees()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun deleteEmployee(employeeId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            projectRepository.deleteEmployee(currentProjectId, employeeId)
                .onSuccess {
                    loadEmployees()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetEmployeeInvited() {
        _uiState.value = _uiState.value.copy(employeeInvited = false)
    }

}
