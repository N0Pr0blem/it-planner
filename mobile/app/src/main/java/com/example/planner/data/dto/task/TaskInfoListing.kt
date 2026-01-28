package com.example.planner.data.dto.task

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskInfoListing(
    var id: Long,
    var name: String,
    @Json(name = "is_completed")
    var isCompleted: Boolean,
    @Json(name = "assign_by")
    var assignBy: String,
    @Json(name = "assign_by_image")
    var assignByImage: String?,
    var status: String? = null
)