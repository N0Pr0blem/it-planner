package com.laba.it_planner.dto.userInfo

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserInfoForTaskDto(
    var profileImage:String? = null,
    var firstName:String? = null,
    var secondName:String? = null,
)