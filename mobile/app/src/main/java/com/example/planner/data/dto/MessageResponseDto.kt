package com.example.planner.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageResponseDto(
    val message: String
)
