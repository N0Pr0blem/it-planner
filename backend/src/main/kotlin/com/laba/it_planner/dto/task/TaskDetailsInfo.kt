package com.laba.it_planner.dto.task

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.dto.userInfo.UserInfoForTaskDto

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TaskDetailsInfo(
    val fromUser: UserInfoForTaskDto,
    val toUser: UserInfoForTaskDto? = null,
    val descriptionFile: String? = null,
)