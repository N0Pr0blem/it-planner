package com.example.planner.data.model.task

enum class TaskStatus(
    val title: String,
) {
    TO_DO("Нужно сделать"),
    IN_PROGRESS("В работе"),
    REVIEW("На код ревью"),
    IN_TEST("В тестировании"),
    DONE("Готова");
}