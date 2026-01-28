package com.example.planner.data.repository

import com.example.planner.data.dto.oauth.AuthRequestDto
import com.example.planner.data.dto.oauth.RegisterRequestDto
import com.example.planner.data.mapper.toDomain
import com.example.planner.data.network.ApiService
import com.example.planner.data.network.TokenManager
import com.example.planner.di.CacheModule
import com.example.planner.domain.model.AuthSession
import com.example.planner.domain.model.RegisterInfo
import com.example.planner.domain.model.SimpleMessage
import com.example.planner.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<AuthSession> {
        return try {
            val response = api.login(AuthRequestDto(username, password))
            if (response.isSuccessful) {
                val body = response.body()
                val session = body?.toDomain()
                if (session != null) {
                    TokenManager.saveToken(session.token, session.expiresAt)
                    CacheModule.clearAllCaches()
                    Result.success(session)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                val errorBody = try {
                    response.errorBody()?.string()
                } catch (e: Exception) {
                    null
                }
                val errorMsg = errorBody ?: "Login failed with code ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(Exception("Connection timeout. Please check your network connection."))
        } catch (e: java.net.UnknownHostException) {
            Result.failure(Exception("Cannot reach server. Please check your network connection."))
        } catch (e: java.io.IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Login failed: ${e.message ?: e.javaClass.simpleName}"))
        }
    }

    override suspend fun register(username: String, password: String, firstName: String): Result<RegisterInfo> {
        return try {
            val response = api.register(RegisterRequestDto(username, password, firstName))
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = try {
                    response.errorBody()?.string()
                } catch (e: Exception) {
                    null
                }
                val errorMsg = errorBody ?: "Registration failed with code ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(Exception("Connection timeout. Please check your network connection."))
        } catch (e: java.net.UnknownHostException) {
            Result.failure(Exception("Cannot reach server. Please check your network connection."))
        } catch (e: java.io.IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Registration failed: ${e.message ?: e.javaClass.simpleName}"))
        }
    }

    override suspend fun verify(username: String, code: String): Result<SimpleMessage> {
        return try {
            val response = api.verify(code, username)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = try {
                    response.errorBody()?.string()
                } catch (e: Exception) {
                    null
                }
                val parsedMessage = parseApiMessage(errorBody)
                val errorMsg = parsedMessage ?: errorBody ?: "Verification failed with code ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(Exception("Connection timeout. Please check your network connection."))
        } catch (e: java.net.UnknownHostException) {
            Result.failure(Exception("Cannot reach server. Please check your network connection."))
        } catch (e: java.io.IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Verification failed: ${e.message ?: e.javaClass.simpleName}"))
        }
    }

    override suspend fun resendVerificationCode(username: String): Result<SimpleMessage> {
        return try {
            val response = api.resendVerificationCode(username)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it.toDomain()) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = try {
                    response.errorBody()?.string()
                } catch (e: Exception) {
                    null
                }
                val parsedMessage = parseApiMessage(errorBody)
                val errorMsg = parsedMessage ?: errorBody ?: "Resend failed with code ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(Exception("Connection timeout. Please check your network connection."))
        } catch (e: java.net.UnknownHostException) {
            Result.failure(Exception("Cannot reach server. Please check your network connection."))
        } catch (e: java.io.IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Resend failed: ${e.message ?: e.javaClass.simpleName}"))
        }
    }

    override fun logout() {
        TokenManager.clearToken()
        CacheModule.clearAllCaches()
    }

    override fun isLoggedIn(): Boolean = TokenManager.isTokenValid()

    override fun currentUserId(): Long? = TokenManager.getUserIdFromToken()

    private fun parseApiMessage(body: String?): String? {
        if (body.isNullOrBlank()) {
            return null
        }
        val messageMatch = Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(body)
        return messageMatch?.groupValues?.getOrNull(1)
    }
}
