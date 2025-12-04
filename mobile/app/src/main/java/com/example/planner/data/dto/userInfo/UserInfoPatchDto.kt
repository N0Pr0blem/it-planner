package com.example.planner.data.dto.userInfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfoPatchDto(
    var secondName: String? = null,
    var lastName: String? = null
)