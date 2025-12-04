package com.example.planner.data.dto.oauth

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequestDto(
    val username: String,
    val password: String,
    val firstName: String
)