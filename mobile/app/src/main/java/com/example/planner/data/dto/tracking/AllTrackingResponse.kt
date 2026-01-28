package com.example.planner.data.dto.tracking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllTrackingResponse(
    @Json(name = "trekking_list")
    var trekkingList: List<TrackingResponseDto>,
    @Json(name = "hour_sum")
    var hourSum: Double
)