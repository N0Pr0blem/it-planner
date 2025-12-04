package com.example.planner.data.dto.tracking

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AllTrekkingResponse(
    var trekkingList :List<TrekkingResponseDto>,
    var hourSum : Double
)