package com.example.planner.domain.model

data class Task(
    val id: Long,
    val projectId: Long,
    val name: String,
    val description: String,
    val status: TaskStatus,
    val urgency: TaskUrgency,
    val complexity: TaskComplexity,
    val isCompleted: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val assignedBy: User?,
    val assignedTo: User?
)
