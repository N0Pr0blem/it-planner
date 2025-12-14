package com.laba.it_planner.dto.task

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TaskInfoListing(
    var id: Long,
    var name: String,
    var isCompleted: Boolean,
    var assignBy: String,
    var assignByImage: String?,
    var status: String?
)