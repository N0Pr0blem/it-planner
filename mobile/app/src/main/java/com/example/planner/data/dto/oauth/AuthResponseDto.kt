package com.example.planner.data.dto.oauth

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AuthResponseDto (
    val token: String? = null,
    val issuedAt: Date? = null,
    val expiresAt: Date? = null,
)