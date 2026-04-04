package com.laba.it_planner.dto.ai;

import com.laba.it_planner.model.ai.AIRequestPattern
import java.time.LocalDateTime

data class AISavedResponseDto(
    val responseId: Long,
    val request: String,
    val response: String,
    val pattern: AIRequestPattern,
    val processingTimeMs: Long,
    val sendDate: LocalDateTime
)
