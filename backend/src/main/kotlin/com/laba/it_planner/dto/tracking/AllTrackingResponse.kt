package com.laba.it_planner.dto.trekking

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.dto.tracking.TrackingResponseDto

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AllTrekkingResponse(
    var trekkingList :List<TrackingResponseDto>,
    var hourSum : Double
)