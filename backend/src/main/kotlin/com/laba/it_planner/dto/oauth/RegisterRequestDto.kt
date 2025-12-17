package com.laba.it_planner.dto.oauth

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RegisterRequestDto(
    @field:NotBlank(message = "{error.user.dto.valid.email.not_blank}")
    @field:Email(message = "{error.user.dto.valid.email.not_email}")
    val username: String,

    @field:NotBlank(message = "{error.user.dto.valid.password.not_blank}")
    @field:Size(max=256, min=8, message = "{error.user.dto.valid.password.size}")
    val password: String,

    @field:NotBlank(message = "{error.user.dto.valid.first_name.not_blank}")
    val firstName: String
)