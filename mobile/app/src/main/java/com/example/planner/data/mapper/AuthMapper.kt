package com.example.planner.data.mapper

import com.example.planner.data.dto.MessageResponseDto
import com.example.planner.data.dto.oauth.AuthResponseDto
import com.example.planner.data.dto.oauth.RegisterResponseDto
import com.example.planner.domain.model.AuthSession
import com.example.planner.domain.model.RegisterInfo
import com.example.planner.domain.model.SimpleMessage

fun AuthResponseDto.toDomain(): AuthSession? {
    val tokenValue = token ?: return null
    return AuthSession(
        token = tokenValue,
        issuedAt = issuedAt,
        expiresAt = expiresAt
    )
}

fun RegisterResponseDto.toDomain(): RegisterInfo =
    RegisterInfo(
        username = username,
        firstName = firstName,
        enabled = enabled,
        role = role
    )

fun MessageResponseDto.toDomain(): SimpleMessage =
    SimpleMessage(message = message)
