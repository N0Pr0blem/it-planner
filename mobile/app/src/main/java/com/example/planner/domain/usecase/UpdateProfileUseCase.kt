package com.example.planner.domain.usecase

import com.example.planner.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(secondName: String?, lastName: String?, imageFile: File?) =
        userRepository.updateProfile(secondName, lastName, imageFile)
}
