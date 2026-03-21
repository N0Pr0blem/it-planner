package com.laba.it_planner.dto.comment

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.dto.userInfo.UserInfoResponseDto

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CommentInfoDto(
    var id: Long? = null,
    var text: String? = null,
    var creationDate: String? = null,
    var taskId: Long? = null,
    var author: UserInfoResponseDto
)