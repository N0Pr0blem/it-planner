package com.laba.it_planner.model.task

enum class TaskUrgency(
    val title: String
) {
    URGENT("Срочно"), MEDIUM("Средний приоритет"), NOT_URGENT("Не срочно")
}