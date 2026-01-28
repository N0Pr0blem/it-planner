package com.example.planner.data.dto.userInfo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfoForTaskDto(
    @Json(name = "id")
    var id: Long? = null,
    @Json(name = "profile_image")
    var profileImage:String? = null,
    @Json(name = "first_name")
    var firstName:String? = null,
    @Json(name = "second_name")
    var secondName:String? = null,
    var lastName: String? = null,
    var username: String? = null,
    @Json(name = "email")
    var email: String? = null
)
