package com.example.planner.data.dto.project

import com.example.planner.data.model.user.OauthUser
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ProjectCreateResponseDto(
    val name: String,
    val creationDate: LocalDateTime,
    val createdUser: OauthUser
)