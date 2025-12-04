package com.example.planner.data.dto.userInfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfoForTaskDto(
    var profileImage:String? = null,
    var firstName:String? = null,
    var secondName:String? = null,
)