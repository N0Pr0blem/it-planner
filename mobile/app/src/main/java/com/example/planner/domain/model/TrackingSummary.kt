package com.example.planner.domain.model

data class TrackingSummary(
    val records: List<TrackingRecord>,
    val totalHours: Double
)
