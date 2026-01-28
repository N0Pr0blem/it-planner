package com.example.planner.domain.model

import java.util.Date

/**
 * Domain representation of an authenticated session.
 */
data class AuthSession(
    val token: String,
    val issuedAt: Date?,
    val expiresAt: Date?
)
