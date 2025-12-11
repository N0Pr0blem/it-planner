package com.example.planner.data.repository

import com.example.planner.data.dto.oauth.AuthRequestDto
import com.example.planner.data.dto.oauth.AuthResponseDto
import com.example.planner.data.dto.oauth.RegisterRequestDto
import com.example.planner.data.dto.oauth.RegisterResponseDto
import com.example.planner.data.network.RetrofitInstance
import com.example.planner.data.network.TokenManager

class AuthRepository {
    private val api = RetrofitInstance.api

    suspend fun login(username: String, password: String): Result<AuthResponseDto> {
        return try {
            val response = api.login(AuthRequestDto(username, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.token != null) {
                    TokenManager.saveToken(body.token, body.expiresAt)
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response"))
                }
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, password: String, firstName: String): Result<RegisterResponseDto> {
        return try {
            val response = api.register(RegisterRequestDto(username, password, firstName))
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        TokenManager.clearToken()
    }

    fun isLoggedIn(): Boolean = TokenManager.isTokenValid()
}
