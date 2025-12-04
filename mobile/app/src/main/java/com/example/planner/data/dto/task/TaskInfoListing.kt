package com.example.planner.data.dto.task

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TaskInfoListing(
    var id: Long,
    var name: String,
    var isCompleted: Boolean,
    var assignBy: String,
    var assignByImage: String?
)