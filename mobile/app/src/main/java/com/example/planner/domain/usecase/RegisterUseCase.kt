package com.example.planner.domain.usecase

import com.example.planner.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(username: String, password: String, firstName: String) =
        authRepository.register(username, password, firstName)
}
