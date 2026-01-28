package com.example.planner.domain.usecase

import com.example.planner.domain.repository.AuthRepository
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    operator fun invoke(): Boolean = authRepository.isLoggedIn()
}
