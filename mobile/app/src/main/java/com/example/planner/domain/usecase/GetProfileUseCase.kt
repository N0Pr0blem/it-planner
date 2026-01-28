package com.example.planner.domain.usecase

import com.example.planner.domain.repository.UserRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getProfile()
}
