package com.example.planner.domain.repository

import com.example.planner.domain.model.AuthSession
import com.example.planner.domain.model.RegisterInfo
import com.example.planner.domain.model.SimpleMessage

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<AuthSession>
    suspend fun register(username: String, password: String, firstName: String): Result<RegisterInfo>
    suspend fun verify(username: String, code: String): Result<SimpleMessage>
    suspend fun resendVerificationCode(username: String): Result<SimpleMessage>
    fun logout()
    fun isLoggedIn(): Boolean
    fun currentUserId(): Long?
}
