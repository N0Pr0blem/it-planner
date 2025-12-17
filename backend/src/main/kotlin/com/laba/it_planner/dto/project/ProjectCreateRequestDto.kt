package com.laba.it_planner.dto.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProjectCreateRequestDto(
    @field:NotBlank(message = "{error.project.name.not_blank}")
    @field:Size(max=64, min=2, message = "{error.project.name.valid.size}")
    val name: String
)
