package com.example.planner.domain.model

data class RepoFile(
    val id: Long,
    val projectRepoId: Long?,
    val type: String,
    val name: String
)
