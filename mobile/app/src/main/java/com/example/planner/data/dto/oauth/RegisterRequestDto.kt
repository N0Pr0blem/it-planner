package com.example.planner.data.dto.oauth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequestDto(
    val username: String,
    val password: String,
    @Json(name = "first_name")
    val firstName: String
)