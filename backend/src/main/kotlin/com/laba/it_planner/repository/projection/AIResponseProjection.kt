package com.laba.it_planner.repository.projection

import java.time.LocalDateTime

interface AIResponseProjection {
    fun getResponseId(): Long
    fun getRequest(): String
    fun getResponse(): String
    fun getPattern(): String
    fun getProcessingTime(): Long
    fun getSendDate(): LocalDateTime
}