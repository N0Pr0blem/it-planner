package com.example.planner.data.dto.task

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskInfoListing(
    var id: Long,
    var name: String,
    var isCompleted: Boolean,
    var assignBy: String,
    var assignByImage: String?
)