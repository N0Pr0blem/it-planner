package com.example.planner.ui.mapper

import com.example.planner.domain.model.TaskFile
import com.example.planner.ui.screens.TaskFileUi

fun TaskFile.toUi(): TaskFileUi =
    TaskFileUi(
        id = id.toString(),
        fileName = name
    )

fun List<TaskFile>.toUi(): List<TaskFileUi> = map { it.toUi() }
