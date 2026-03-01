package com.laba.it_planner.dto.project

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class ProjectListingDto(
    val id: Long,
    val name: String,
    val storageId : Long,
    val creationDate: LocalDateTime,
)