package com.example.planner.domain.model

import java.time.LocalDate

data class TrackingRecord(
    val id: Long,
    val date: LocalDate,
    val hours: Double,
    val employeeFirstName: String?,
    val employeeSecondName: String?,
    val taskDetailsId: Long
)
