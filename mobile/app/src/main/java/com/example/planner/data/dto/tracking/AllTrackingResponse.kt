package com.example.planner.data.dto.tracking

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllTrackingResponse(
    var trekkingList: List<TrackingResponseDto>,
    var hourSum: Double
)