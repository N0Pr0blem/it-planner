package com.laba.it_planner.service.ai

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.ai.AIRequestDto
import com.laba.it_planner.dto.ai.AIRequestResponseDto
import com.laba.it_planner.dto.ai.AISavedResponseDto
import java.security.Principal
import java.util.*

interface AIRequestService {
    fun sendRequest(aIRequestDto: AIRequestDto, principal: Principal): AIRequestResponseDto
    fun saveResponse(aIRequestDto: AIRequestResponseDto, locale: Locale): MessageResponseDto
    fun getAll(principal: Principal):List<AISavedResponseDto>
}