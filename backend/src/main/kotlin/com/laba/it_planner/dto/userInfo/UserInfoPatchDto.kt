package com.laba.it_planner.dto.userInfo

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserInfoPatchDto(
    var secondName: String? = null,
    var lastName: String? = null
)