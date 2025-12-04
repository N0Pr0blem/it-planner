package com.example.planner.data.dto.task

import com.example.planner.data.dto.userInfo.UserInfoForTaskDto

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TaskDetailsInfo(
    val fromUser: UserInfoForTaskDto,
    val toUser: UserInfoForTaskDto? = null,
    val descriptionFile: String? = null,
)