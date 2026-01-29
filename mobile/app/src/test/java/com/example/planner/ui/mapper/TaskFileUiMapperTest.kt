package com.example.planner.ui.mapper

import com.example.planner.domain.model.TaskFile
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskFileUiMapperTest {

    @Test
    fun `TaskFile maps to TaskFileUi`() {
        val file = TaskFile(id = 10, taskId = 4, name = "design.pdf", type = "PDF")

        val ui = file.toUi()

        assertEquals("10", ui.id)
        assertEquals("design.pdf", ui.fileName)
    }
}
