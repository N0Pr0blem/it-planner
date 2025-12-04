package com.example.planner.data.dto.userInfo

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserInfoForTaskDto(
    var profileImage:String? = null,
    var firstName:String? = null,
    var secondName:String? = null,
)