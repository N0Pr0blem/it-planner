package com.example.planner.domain.usecase

import com.example.planner.domain.repository.AuthRepository
import javax.inject.Inject

class ResendVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(username: String) =
        authRepository.resendVerificationCode(username)
}
