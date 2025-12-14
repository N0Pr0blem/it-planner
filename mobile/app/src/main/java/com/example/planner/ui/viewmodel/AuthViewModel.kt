package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(isLoggedIn = repository.isLoggedIn())
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.login(username, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        is com.example.planner.domain.exception.AuthenticationException -> "Authentication failed: ${e.message}"
                        else -> "Operation failed: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun register(username: String, password: String, firstName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.register(username, password, firstName)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isRegistered = true)
                }
                .onFailure { e ->
                    val errorMessage = when (e) {
                        is com.example.planner.domain.exception.ValidationException -> e.message
                        is com.example.planner.domain.exception.NetworkException -> "Network error: ${e.message}"
                        is com.example.planner.domain.exception.AuthenticationException -> "Authentication failed: ${e.message}"
                        else -> "Operation failed: ${e.message}"
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, error = errorMessage)
                }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = AuthUiState(isLoggedIn = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetRegistrationState() {
        _uiState.value = _uiState.value.copy(isRegistered = false)
    }
}