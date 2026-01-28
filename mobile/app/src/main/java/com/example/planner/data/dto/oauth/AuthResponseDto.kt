package com.example.planner.data.dto.oauth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AuthResponseDto (
    val token: String? = null,
    @Json(name = "issued_at")
    val issuedAt: Date? = null,
    @Json(name = "expires_at")
    val expiresAt: Date? = null,
)