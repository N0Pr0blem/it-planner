package com.example.planner.data.dto.oauth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponseDto(
    val username: String,
    @Json(name = "first_name")
    val firstName: String? = null,
    val enabled: Boolean,
    val role: String
)