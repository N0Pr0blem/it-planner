package com.laba.it_planner.dto.oauth

class RegisterResponseDto(
    val username: String,
    val firstName: String? = null,
    val enabled: Boolean,
    val role: String
)