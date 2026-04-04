package com.laba.it_planner.dto.ai

import com.laba.it_planner.model.ai.AIRequestPattern

data class AIRequestResponseDto(
    val requestId: Long,
    val response: String,
    val pattern: AIRequestPattern,
    val originalRequest: String,
    val processingTimeMs: Long
)