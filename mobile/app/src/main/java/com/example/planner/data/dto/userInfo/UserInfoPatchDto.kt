package com.example.planner.data.dto.userInfo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfoPatchDto(
    @Json(name = "second_name")
    var secondName: String? = null,
    @Json(name = "last_name")
    var lastName: String? = null
)