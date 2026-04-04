package com.laba.it_planner.dto.ai

import com.laba.it_planner.model.ai.AIRequestPattern

data class AIRequestDto(
    var pattern: AIRequestPattern,
    var request: String
)