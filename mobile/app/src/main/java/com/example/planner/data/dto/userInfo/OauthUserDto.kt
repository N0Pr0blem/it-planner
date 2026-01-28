package com.example.planner.data.dto.userInfo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OauthUserDto(
    val id: Long? = null,
    val username: String? = null,
    val password: String? = null,
    val enabled: Boolean = false,
    @Json(name = "verification_code")
    val verificationCode: String? = null,
    @Json(name = "oauth_role")
    val role: String = "USER"
)


