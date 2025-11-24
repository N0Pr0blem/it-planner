package com.laba.it_planner.model.task

enum class TaskStatus(
    val title: String,
) {
    TO_DO("Нужно сделать"), IN_PROGRESS("В работе"), REVIEW("На код ревью"), DONE("Сделано");
}
