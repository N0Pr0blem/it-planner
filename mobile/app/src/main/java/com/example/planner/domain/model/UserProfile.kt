package com.example.planner.domain.model

import java.time.LocalDateTime

data class UserProfile(
    val username: String,
    val firstName: String?,
    val secondName: String?,
    val lastName: String?,
    val profileImage: String?,
    val registrationDate: LocalDateTime?,
    val projects: List<Project>,
    val tasks: List<Task>
)
