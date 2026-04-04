package com.laba.it_planner.service.ai

import com.laba.it_planner.dto.ai.AIRequestDto
import com.laba.it_planner.dto.ai.AIRequestResponseDto
import java.security.Principal

interface AIRequestService {
    fun sendRequest(aIRequestDto: AIRequestDto, principal: Principal):AIRequestResponseDto
}