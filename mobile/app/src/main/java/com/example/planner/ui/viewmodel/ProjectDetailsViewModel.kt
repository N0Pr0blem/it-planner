package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.domain.model.ProjectRole
import com.example.planner.domain.model.ProjectMember
import com.example.planner.ui.screens.ProjectTaskUi
import com.example.planner.ui.screens.RepoFileUi
import com.example.planner.ui.mapper.toProjectTaskUiList
import com.example.planner.domain.usecase.DeleteEmployeeUseCase
import com.example.planner.domain.usecase.GetProjectEmployeesUseCase
import com.example.planner.domain.usecase.GetProjectTasksUseCase
import com.example.planner.domain.usecase.GetProjectsUseCase
import com.example.planner.domain.usecase.GetProjectUseCase
import com.example.planner.domain.usecase.InviteEmployeeUseCase
import com.example.planner.domain.usecase.UpdateEmployeeRoleUseCase
import com.example.planner.domain.usecase.GetRepoFilesUseCase
import com.example.planner.domain.usecase.UploadRepoFileUseCase
import com.example.planner.domain.usecase.DeleteRepoFileUseCase
import com.example.planner.domain.usecase.DownloadRepoFileUseCase
import com.example.planner.domain.usecase.GetCurrentUserIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class ProjectDetailsUiState(
    val isLoading: Boolean = false,
    val projectName: String = "",
    val tasks: List<ProjectTaskUi> = emptyList(),
    val employees: List<ProjectMember> = emptyList(),
    val repoFiles: List<RepoFileUi> = emptyList(),
    val error: String? = null,
    val taskCreated: Boolean = false,
    val employeeInvited: Boolean = false,
    val memberRoleUpdated: Boolean = false
)

sealed interface RepoEvent {
    data class Downloaded(val filename: String, val bytes: ByteArray) : RepoEvent
    data class Error(val message: String) : RepoEvent
}

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val getProjectTasksUseCase: GetProjectTasksUseCase,
    private val getProjectEmployeesUseCase: GetProjectEmployeesUseCase,
    private val inviteEmployeeUseCase: InviteEmployeeUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val updateEmployeeRoleUseCase: UpdateEmployeeRoleUseCase,
    private val getProjectUseCase: GetProjectUseCase,
    private val getRepoFilesUseCase: GetRepoFilesUseCase,
    private val uploadRepoFileUseCase: UploadRepoFileUseCase,
    private val deleteRepoFileUseCase: DeleteRepoFileUseCase,
    private val downloadRepoFileUseCase: DownloadRepoFileUseCase,
    getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectDetailsUiState())
    val uiState: StateFlow<ProjectDetailsUiState> = _uiState.asStateFlow()

    private val _repoEvents = MutableSharedFlow<RepoEvent>()
    val repoEvents: SharedFlow<RepoEvent> = _repoEvents.asSharedFlow()

    private var currentProjectId: Long = 0
    private val removedEmployeeIds = mutableSetOf<Long>()
    private val currentUserId: Long? = getCurrentUserIdUseCase()

    fun loadProjectDetails(projectId: Long) {
        if (currentProjectId != projectId) {
            removedEmployeeIds.clear()
        }
        currentProjectId = projectId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val projectResult = withContext(Dispatchers.IO) { getProjectUseCase(projectId) }
            projectResult
                .onSuccess { project ->
                    _uiState.value = _uiState.value.copy(projectName = project.name)
                }
                .onFailure { e ->
                    val projectsResult = withContext(Dispatchers.IO) {
                        getProjectsUseCase()
                    }
                    projectsResult
                        .onSuccess { projects ->
                            val project = projects.find { it.id.toLong() == projectId }
                            if (project != null) {
                                _uiState.value = _uiState.value.copy(projectName = project.name)
                            } else {
                                _uiState.value = _uiState.value.copy(projectName = "Project $projectId")
                            }
                        }
                        .onFailure { e2 ->
                            _uiState.value = _uiState.value.copy(projectName = "Project $projectId")
                        }
                }
        }
        loadTasks(projectId)
    }

    fun loadTasks(projectId: Long = currentProjectId, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val tasksResult = withContext(Dispatchers.IO) {
                getProjectTasksUseCase(projectId)
            }
            tasksResult
                .onSuccess { tasks ->
                    val tasksUi = withContext(Dispatchers.Default) {
                        tasks.toProjectTaskUiList()
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, tasks = tasksUi)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to load project details: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage ?: "Failed to load tasks")
                }
        }
    }

    fun loadEmployees(projectId: Long = currentProjectId, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val employeesResult = withContext(Dispatchers.IO) {
                getProjectEmployeesUseCase(projectId)
            }
            employeesResult
                .onSuccess { employees ->
                    val filtered = employees.filterNot { removedEmployeeIds.contains(it.id) }
                    _uiState.value = _uiState.value.copy(isLoading = false, employees = filtered)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to load project details: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun loadRepoFiles(projectId: Long = currentProjectId) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val repoFilesResult = withContext(Dispatchers.IO) { getRepoFilesUseCase(projectId) }
            repoFilesResult
                .onSuccess { files ->
                    val repoUi = files.map { RepoFileUi(id = it.id.toString(), filename = it.name) }
                    _uiState.value = _uiState.value.copy(isLoading = false, repoFiles = repoUi, error = null)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to load project details: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun uploadRepoFile(file: File, displayName: String?, contentType: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val uploadResult = withContext(Dispatchers.IO) {
                uploadRepoFileUseCase(currentProjectId, file, displayName, contentType)
            }
            uploadResult
                .onSuccess {
                    loadRepoFiles()
                }
                .onFailure { e ->
                    val message = when {
                        e.message?.contains("HTTP 403") == true -> "No permission to upload to this repository."
                        else -> e.message ?: "Failed to upload file"
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = message
                    )
                    loadRepoFiles()
                }
        }
    }

    fun deleteRepoFile(fileId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val deleteResult = withContext(Dispatchers.IO) {
                deleteRepoFileUseCase(currentProjectId, fileId)
            }
            deleteResult
                .onSuccess {
                    loadRepoFiles()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to delete file"
                    )
                }
        }
    }

    fun downloadRepoFile(fileId: Long, filename: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val downloadResult = withContext(Dispatchers.IO) {
                downloadRepoFileUseCase(currentProjectId, fileId)
            }
            downloadResult
                .onSuccess { bytes ->
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _repoEvents.emit(RepoEvent.Downloaded(filename, bytes))
                }
                .onFailure { e ->
                    val message = when {
                        e.message?.contains("HTTP 403") == true -> "No permission to download this file."
                        else -> e.message ?: "Failed to download file"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _repoEvents.emit(RepoEvent.Error(message))
                }
        }
    }

    fun inviteEmployee(username: String, role: ProjectRole) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val inviteResult = withContext(Dispatchers.IO) {
                inviteEmployeeUseCase(currentProjectId, username, role)
            }
            inviteResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, employeeInvited = true)
                    loadEmployees(forceRefresh = true)
                }
                .onFailure { e ->
                    val rawMessage = e.message.orEmpty()
                    val errorMessage = when {
                        rawMessage.contains("Failed to invite employee") || rawMessage.contains("No value present") ->
                            "User not found. Use the registered email."
                        e is com.example.planner.domain.exception.ValidationException -> e.message
                        e is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to invite employee: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun deleteEmployee(employeeId: Long) {
        viewModelScope.launch {
            if (currentUserId != null && currentUserId == employeeId) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "You can't remove yourself from the project"
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val deleteResult = withContext(Dispatchers.IO) {
                deleteEmployeeUseCase(currentProjectId, employeeId)
            }
            deleteResult
                .onSuccess {
                    removedEmployeeIds.add(employeeId)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        employees = _uiState.value.employees.filterNot { it.id == employeeId }
                    )
                    loadEmployees(forceRefresh = true)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to load project details: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun updateEmployeeRole(employeeId: Long, role: ProjectRole) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val updateResult = withContext(Dispatchers.IO) {
                updateEmployeeRoleUseCase(currentProjectId, employeeId, role)
            }
            updateResult
                .onSuccess { updated ->
                    val nextEmployees = _uiState.value.employees.map { member ->
                        if (member.id == employeeId) member.copy(role = updated.role) else member
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        employees = nextEmployees,
                        memberRoleUpdated = true
                    )
                    loadEmployees(forceRefresh = true)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to update employee role: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetEmployeeInvited() {
        _uiState.value = _uiState.value.copy(employeeInvited = false)
    }

    fun resetMemberRoleUpdated() {
        _uiState.value = _uiState.value.copy(memberRoleUpdated = false)
    }

}
