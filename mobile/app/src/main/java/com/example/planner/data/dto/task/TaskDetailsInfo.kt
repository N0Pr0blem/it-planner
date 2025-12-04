package com.example.planner.data.dto.task

import com.example.planner.data.dto.userInfo.UserInfoForTaskDto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskDetailsInfo(
    val fromUser: UserInfoForTaskDto,
    val toUser: UserInfoForTaskDto? = null,
    val descriptionFile: String? = null,
)