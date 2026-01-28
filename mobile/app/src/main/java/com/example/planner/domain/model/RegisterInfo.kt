package com.example.planner.domain.model

/**
 * Domain representation of registration result.
 */
data class RegisterInfo(
    val username: String,
    val firstName: String?,
    val enabled: Boolean,
    val role: String
)
