package com.example.planner.ui.extensions

import androidx.compose.ui.graphics.Color
import com.example.planner.domain.model.TaskComplexity
import com.example.planner.domain.model.TaskStatus
import com.example.planner.domain.model.TaskUrgency

val TaskStatus.title: String
    get() = when (this) {
        TaskStatus.TO_DO -> "To do"
        TaskStatus.IN_PROGRESS -> "In progress"
        TaskStatus.REVIEW -> "In review"
        TaskStatus.IN_TEST -> "In test"
        TaskStatus.DONE -> "Done"
    }

fun TaskStatus.dotColor(): Color = when (this) {
    TaskStatus.TO_DO -> Color(0xFF6B7280)
    TaskStatus.IN_PROGRESS -> Color(0xFF3B82F6)
    TaskStatus.REVIEW -> Color(0xFF8B5CF6)
    TaskStatus.IN_TEST -> Color(0xFFF59E0B)
    TaskStatus.DONE -> Color(0xFF16A34A)
}

val TaskUrgency.title: String
    get() = when (this) {
        TaskUrgency.URGENT -> "Urgent"
        TaskUrgency.MEDIUM -> "Medium"
        TaskUrgency.NOT_URGENT -> "Not urgent"
    }

val TaskComplexity.title: String
    get() = when (this) {
        TaskComplexity.HARD -> "Hard"
        TaskComplexity.MEDIUM -> "Medium"
        TaskComplexity.EASY -> "Easy"
    }
