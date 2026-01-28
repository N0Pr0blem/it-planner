package com.example.planner.domain.model

// Domain model for project members (keeps UI/data layers decoupled)
data class ProjectMember(
    val id: Long,
    val role: ProjectRole,
    val firstName: String?,
    val secondName: String?,
    val profileImageUrl: String?
)
