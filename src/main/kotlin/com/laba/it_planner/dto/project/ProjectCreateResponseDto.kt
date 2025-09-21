package com.laba.it_planner.dto.project

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.model.user.OauthUser
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProjectCreateResponseDto(
    val name: String,
    val creationDate: LocalDateTime,
    val createdUser: OauthUser
)