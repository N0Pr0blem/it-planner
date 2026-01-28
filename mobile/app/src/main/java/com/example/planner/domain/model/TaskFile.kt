package com.example.planner.domain.model

data class TaskFile(
    val id: Long,
    val taskId: Long?,
    val name: String,
    val type: String?
)
