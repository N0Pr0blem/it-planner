package com.example.planner.data.dto.project

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProjectListingDto(
    val id: Long,
    val name: String
)