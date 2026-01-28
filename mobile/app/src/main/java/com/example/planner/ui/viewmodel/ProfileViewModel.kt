package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.planner.domain.usecase.GetProfileUseCase
import com.example.planner.domain.usecase.UpdateProfileUseCase

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: com.example.planner.domain.model.UserProfile? = null,
    val error: String? = null,
    val profileUpdated: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {
    private val logTag = "ProfileViewModel"

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val profileResult = withContext(Dispatchers.IO) { getProfileUseCase() }
            profileResult
                .onSuccess { profile ->
                    _uiState.value = _uiState.value.copy(isLoading = false, profile = profile)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to load profile: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun updateProfile(secondName: String?, lastName: String?, imageFile: java.io.File?) {
        viewModelScope.launch {
            Log.i(logTag, "updateProfile secondName=$secondName lastName=$lastName image=${imageFile?.name}")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val updateResult = withContext(Dispatchers.IO) { updateProfileUseCase(secondName, lastName, imageFile) }
            updateResult
                .onSuccess { profile ->
                    val refreshedResult = withContext(Dispatchers.IO) { getProfileUseCase() }
                    val nextProfile = refreshedResult.getOrNull() ?: profile
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profile = nextProfile,
                        profileUpdated = true
                    )
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        else -> "Failed to update profile: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
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
