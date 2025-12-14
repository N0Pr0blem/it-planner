package com.laba.it_planner.dto.trekking

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AllTrekkingResponse(
    var trekkingList :List<TrekkingResponseDto>,
    var hourSum : Double
)