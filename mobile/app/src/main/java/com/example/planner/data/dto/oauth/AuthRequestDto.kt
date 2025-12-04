package com.example.planner.data.dto.oauth

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthRequestDto (
    val username: String,
    val password: String
)