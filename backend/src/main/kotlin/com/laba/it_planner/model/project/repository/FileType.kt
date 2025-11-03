package com.laba.it_planner.model.project.repository

enum class FileType(
    val prefix: String
) {
    PROFILE("profile/"),
    TASK("tasks/"),
    REPOSITORY("repository/")
}