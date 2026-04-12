package com.laba.it_planner.dto.tracking

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AllTrekkingResponse(
    var trekkingList: List<TrackingResponseDto>,
    var hourSum: Double
)