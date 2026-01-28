package com.example.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planner.domain.usecase.IsLoggedInUseCase
import com.example.planner.domain.usecase.LoginUseCase
import com.example.planner.domain.usecase.LogoutUseCase
import com.example.planner.domain.usecase.RegisterUseCase
import com.example.planner.domain.usecase.ResendVerificationUseCase
import com.example.planner.domain.usecase.VerifyUseCase
import com.example.planner.domain.usecase.GetCurrentUserIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
    val isVerified: Boolean = false,
    val lastRegisteredUsername: String = "",
    val currentUserId: Long? = null,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val verifyUseCase: VerifyUseCase,
    private val resendVerificationUseCase: ResendVerificationUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            isLoggedIn = isLoggedInUseCase(),
            currentUserId = getCurrentUserIdUseCase()
        )
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val loginResult = withContext(Dispatchers.IO) {
                loginUseCase(username, password)
            }
            loginResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        currentUserId = getCurrentUserIdUseCase()
                    )
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
            val registerResult = withContext(Dispatchers.IO) {
                registerUseCase(username, password, firstName)
            }
            registerResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRegistered = true,
                        lastRegisteredUsername = username
                    )
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
        logoutUseCase()
        _uiState.value = AuthUiState(isLoggedIn = false, currentUserId = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetRegistrationState() {
        _uiState.value = _uiState.value.copy(isRegistered = false)
    }

    fun verify(username: String, code: String) {
        if (username.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Username is missing")
            return
        }
        if (code.length != 4) {
            _uiState.value = _uiState.value.copy(error = "Enter the 4-digit code")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val verifyResult = withContext(Dispatchers.IO) {
                verifyUseCase(username, code)
            }
            verifyResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isVerified = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = mapVerificationError(e.message)
                    )
                }
        }
    }

    fun resendVerificationCode(username: String) {
        if (username.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Username is missing")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val resendResult = withContext(Dispatchers.IO) {
                resendVerificationUseCase(username)
            }
            resendResult
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Code sent. Check your email."
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = mapVerificationError(e.message)
                    )
                }
        }
    }

    fun resetVerificationState() {
        _uiState.value = _uiState.value.copy(isVerified = false)
    }

    private fun mapVerificationError(message: String?): String {
        val raw = message.orEmpty()
        return when {
            raw.contains("code 403", ignoreCase = true) || raw.contains("403", ignoreCase = true) ->
                "Resend blocked. Check the email and try again."
            raw.contains("Wrong verification code", ignoreCase = true) ->
                "Wrong code. Check the email or tap Resend."
            raw.contains("USER_NOT_FOUND", ignoreCase = true) ->
                "User not found. Register again."
            raw.contains("Account already verified", ignoreCase = true) ->
                "Account already verified. You can log in."
            raw.isNotBlank() -> raw
            else -> "Verification failed. Try again."
        }
    }
}
