package com.example.planner.data.dto.project

import com.example.planner.data.dto.userInfo.OauthUserDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ProjectCreateResponseDto(
    val name: String,
    @Json(name = "creation_date")
    val creationDate: LocalDateTime,
    @Json(name = "created_user")
    val createdUser: OauthUserDto
)