package com.example.planner.data.dto.userInfo



@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserInfoPatchDto(
    var secondName: String? = null,
    var lastName: String? = null
)