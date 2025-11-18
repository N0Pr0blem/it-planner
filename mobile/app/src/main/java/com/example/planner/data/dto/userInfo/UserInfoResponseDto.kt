package com.example.planner.data.dto.userInfo

import java.time.LocalDateTime

data class UserInfoResponseDto(
    val username: String,
    val firstName: String? = null,
    val secondName: String? = null,
    val lastName: String? = null,
    val profileImage: String? = null,
    val registrationDate: LocalDateTime? = null
)