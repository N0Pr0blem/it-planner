package com.example.planner.ui.mapper

import com.example.planner.domain.model.Task
import com.example.planner.domain.model.TaskStatus
import com.example.planner.ui.screens.ProjectTaskUi

fun Task.toProjectTaskUi(): ProjectTaskUi {
    fun sanitize(name: String?): String {
        return name
            ?.split(' ')
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() && it.lowercase() != "null" }
            ?.joinToString(" ")
            .orEmpty()
    }

    val assigneeName = sanitize(assignedTo?.let { "${it.firstName ?: ""} ${it.secondName ?: ""}" })
        .ifBlank { sanitize(assignedBy?.let { "${it.firstName ?: ""} ${it.secondName ?: ""}" }) }
    return ProjectTaskUi(
        id = id.toString(),
        title = name,
        assignee = assigneeName,
        status = status
    )
}

fun List<Task>.toProjectTaskUiList(): List<ProjectTaskUi> =
    map { it.toProjectTaskUi() }
