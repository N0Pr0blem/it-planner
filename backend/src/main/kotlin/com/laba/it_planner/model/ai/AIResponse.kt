package com.laba.it_planner.model.ai

import jakarta.persistence.*

@Entity
@Table(name = "ai_response")
class AIResponse(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    var request: AIRequest,

    @Column(name = "response")
    var response: String,

    @Column(name = "processing_time")
    val processingTimeMs: Long
)