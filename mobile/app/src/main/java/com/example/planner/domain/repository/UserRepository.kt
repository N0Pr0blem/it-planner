package com.example.planner.domain.repository

import com.example.planner.domain.model.UserProfile
import java.io.File

interface UserRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateProfile(
        secondName: String? = null,
        lastName: String? = null,
        imageFile: File? = null
    ): Result<UserProfile>
}
