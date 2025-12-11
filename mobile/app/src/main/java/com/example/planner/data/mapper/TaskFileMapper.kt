package com.example.planner.data.mapper

import com.example.planner.data.dto.task.TaskFileDto
import com.example.planner.ui.screens.TaskFileUi

fun TaskFileDto.toUi(): TaskFileUi {
    return TaskFileUi(
        id = id.toString(),
        fileName = name
    )
}

fun List<TaskFileDto>.toUi(): List<TaskFileUi> {
    return map { it.toUi() }
}