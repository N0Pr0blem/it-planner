package com.example.planner.ui.mapper

import com.example.planner.domain.model.Task
import com.example.planner.domain.model.TaskComplexity
import com.example.planner.domain.model.TaskStatus
import com.example.planner.domain.model.TaskUrgency
import com.example.planner.domain.model.User
import com.example.planner.ui.screens.ProjectTaskUi
import org.junit.Assert.assertEquals
import org.junit.Test

class ProjectTaskUiMapperTest {

    @Test
    fun `Task maps to ProjectTaskUi with assignee fallback`() {
        val task = Task(
            id = 7,
            projectId = 3,
            name = "Implement API",
            description = "desc",
            status = TaskStatus.IN_PROGRESS,
            urgency = TaskUrgency.MEDIUM,
            complexity = TaskComplexity.MEDIUM,
            isCompleted = false,
            createdAt = "",
            updatedAt = "",
            assignedBy = User(1, "lead", "Lead", null, null, null, null),
            assignedTo = null
        )

        val ui: ProjectTaskUi = task.toProjectTaskUi()

        assertEquals("7", ui.id)
        assertEquals("Implement API", ui.title)
        assertEquals("Lead", ui.assignee) // falls back to assignedBy if assignedTo empty
        assertEquals(TaskStatus.IN_PROGRESS, ui.status)
    }
}
