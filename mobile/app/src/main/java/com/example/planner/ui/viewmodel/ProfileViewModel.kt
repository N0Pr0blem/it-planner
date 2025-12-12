package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.dto.task.TaskInfoListing
import com.example.planner.data.dto.userInfo.UserInfoResponseDto
import com.example.planner.data.repository.ProjectRepositoryLegacy
import com.example.planner.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UserInfoResponseDto? = null,
    val error: String? = null,
    val profileUpdated: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val projectRepository = ProjectRepositoryLegacy()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            // Загружаем профиль пользователя
            userRepository.getProfile()
                .onSuccess { profile ->
                    // Загружаем проекты пользователя
                    projectRepository.getProjects()
                        .onSuccess { projects ->
                            // Загружаем задачи пользователя (пока заглушка, так как нет эндпоинта для задач пользователя)
                            // В реальном приложении нужно добавить эндпоинт для получения задач пользователя
                            val tasks = emptyList<TaskInfoListing>()
                            
                            // Обновляем профиль с проектами и задачами
                            val updatedProfile = profile.copy(projects = projects, tasks = tasks)
                            _uiState.value = _uiState.value.copy(isLoading = false, profile = updatedProfile)
                        }
                        .onFailure { e ->
                            _uiState.value = _uiState.value.copy(isLoading = false, profile = profile, error = e.message)
                        }
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun updateProfile(firstName: String?, secondName: String?, lastName: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            userRepository.updateProfile(firstName, secondName, lastName)
                .onSuccess { profile ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profile = profile,
                        profileUpdated = true
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetProfileUpdated() {
        _uiState.value = _uiState.value.copy(profileUpdated = false)
    }
}
