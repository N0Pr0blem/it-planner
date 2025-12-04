package com.example.planner.data.dto.project

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProjectCreateRequestDto(
    val name: String
)
