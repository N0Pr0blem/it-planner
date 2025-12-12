package com.example.planner.domain.model

data class User(
    val id: Long,
    val username: String,
    val firstName: String?,
    val secondName: String?,
    val lastName: String?,
    val email: String?,
    val profileImageUrl: String?
)