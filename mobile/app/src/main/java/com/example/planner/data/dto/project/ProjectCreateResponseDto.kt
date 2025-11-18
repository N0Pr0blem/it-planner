package com.example.planner.data.dto.project

import com.example.planner.data.model.user.OauthUser
import java.time.LocalDateTime


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProjectCreateResponseDto(
    val name: String,
    val creationDate: LocalDateTime,
    val createdUser: OauthUser
)