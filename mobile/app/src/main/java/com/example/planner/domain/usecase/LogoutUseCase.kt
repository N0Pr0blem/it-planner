package com.example.planner.domain.usecase

import com.example.planner.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    operator fun invoke() = authRepository.logout()
}
