package com.example.planner.data.model.task

enum class TaskStatus(
    val title: String,
) {
    TO_DO("To do"),
    IN_PROGRESS("In progress"),
    REVIEW("In review"),
    IN_TEST("In testing"),
    DONE("Done");
}
