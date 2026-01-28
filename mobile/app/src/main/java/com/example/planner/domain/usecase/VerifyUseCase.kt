package com.example.planner.domain.usecase

import com.example.planner.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(username: String, code: String) =
        authRepository.verify(username, code)
}
