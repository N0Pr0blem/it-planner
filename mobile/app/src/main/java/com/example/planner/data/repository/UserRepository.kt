package com.example.planner.data.repository

import com.example.planner.data.dto.userInfo.UserInfoPatchDto
import com.example.planner.data.dto.userInfo.UserInfoResponseDto
import com.example.planner.data.network.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserRepository {
    private val api = RetrofitInstance.api

    suspend fun getProfile(): Result<UserInfoResponseDto> {
        return try {
            val response = api.getProfileInfo()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        firstName: String? = null,
        secondName: String? = null,
        lastName: String? = null,
        imageFile: File? = null
    ): Result<UserInfoResponseDto> {
        return try {
            val patchDto = UserInfoPatchDto(
                firstName = firstName,
                secondName = secondName,
                lastName = lastName
            )
            val imagePart = imageFile?.let {
                val requestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("multipartFile", it.name, requestBody)
            }
            val response = api.updateProfile(patchDto, imagePart)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Failed to update profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
