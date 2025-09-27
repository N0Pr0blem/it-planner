package com.laba.it_planner.model.project.repository

enum class FileType(
    val prefix: String
) {
    IMAGE("images/"),
    TASK("tasks/"),
    REPOSITORY("repository/")
}