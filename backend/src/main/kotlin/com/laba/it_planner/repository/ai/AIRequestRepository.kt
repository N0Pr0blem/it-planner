package com.laba.it_planner.repository.ai

import com.laba.it_planner.model.ai.AIRequest
import org.springframework.data.jpa.repository.JpaRepository

interface AIRequestRepository: JpaRepository<AIRequest, Long> {
}