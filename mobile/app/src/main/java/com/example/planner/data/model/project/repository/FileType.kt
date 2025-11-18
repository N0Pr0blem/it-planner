package com.example.planner.data.model.project.repository

enum class FileType(
    val prefix: String
) {
    PROFILE("profile/"),
    TASK("tasks/"),
    REPOSITORY("repository/")
}