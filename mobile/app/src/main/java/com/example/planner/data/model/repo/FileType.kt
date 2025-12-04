package com.example.planner.data.model.repo

enum class FileType(
    val prefix: String
) {
    PROFILE("/profile/"),
    TASK("/tasks/"),
    REPOSITORY("/repository/")
}