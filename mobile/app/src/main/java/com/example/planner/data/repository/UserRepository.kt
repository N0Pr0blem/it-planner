package com.example.planner.data.repository

import android.util.Log
import com.example.planner.data.network.ApiService
import com.example.planner.data.mapper.toDomain
import com.example.planner.domain.model.UserProfile
import com.example.planner.domain.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UserRepository {
    private val logTag = "UserRepository"

    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val response = api.getProfileInfo()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(
        secondName: String? = null,
        lastName: String? = null,
        imageFile: File? = null
    ): Result<UserProfile> {
        return try {
            Log.i(logTag, "updateProfile secondName=$secondName lastName=$lastName image=${imageFile?.name}")
            val secondNameBody = secondName?.toRequestBody("text/plain".toMediaTypeOrNull())
            val lastNameBody = lastName?.toRequestBody("text/plain".toMediaTypeOrNull())
            val imagePart = imageFile?.let {
                val requestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", it.name, requestBody)
            }
            val response = api.updateProfile(secondNameBody, lastNameBody, imagePart)
            if (response.isSuccessful) {
                Log.i(logTag, "updateProfile success code=${response.code()}")
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(logTag, "updateProfile failed code=${response.code()} body=$errorBody")
                Result.failure(Exception(errorBody ?: "Failed to update profile"))
            }
        } catch (e: Exception) {
            Log.e(logTag, "updateProfile exception=${e.message}", e)
            Result.failure(e)
        }
    }
}
