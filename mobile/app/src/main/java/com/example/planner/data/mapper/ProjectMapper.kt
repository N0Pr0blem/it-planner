package com.example.planner.data.mapper

import com.example.planner.data.dto.project.ProjectListingDto
import com.example.planner.ui.screens.ProjectUi

fun ProjectListingDto.toUi(): ProjectUi {
    return ProjectUi(
        id = id.toString(),
        name = name,
        date = "" // Пока оставляем пустым, можно добавить форматирование даты позже
    )
}

fun List<ProjectListingDto>.toUi(): List<ProjectUi> {
    return map { it.toUi() }
}