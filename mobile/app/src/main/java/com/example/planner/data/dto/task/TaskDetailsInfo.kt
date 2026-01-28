package com.example.planner.data.dto.task

import com.example.planner.data.dto.userInfo.UserInfoForTaskDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskDetailsInfo(
    @Json(name = "from_user")
    val fromUser: UserInfoForTaskDto,
    @Json(name = "to_user")
    val toUser: UserInfoForTaskDto? = null,
    @Json(name = "description_file")
    val descriptionFile: String? = null,
)