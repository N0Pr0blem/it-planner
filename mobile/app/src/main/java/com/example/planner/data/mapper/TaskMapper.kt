package com.example.planner.data.mapper

import com.example.planner.data.dto.task.TaskInfoListing
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.ui.screens.ProjectTaskUi

fun TaskInfoListing.toUi(): ProjectTaskUi {
    return ProjectTaskUi(
        id = id.toString(),
        title = name,
        assignee = assignBy,
        status = if (isCompleted) TaskStatus.DONE else TaskStatus.TO_DO
    )
}

fun List<TaskInfoListing>.toUi(): List<ProjectTaskUi> {
    return map { it.toUi() }
}