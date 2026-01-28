package com.example.planner.domain.usecase

import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.data.model.task.TaskUrgency
import com.example.planner.domain.exception.ValidationException
import com.example.planner.domain.model.Task
import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UpdateTaskUseCaseTest {

    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        updateTaskUseCase = UpdateTaskUseCase(projectRepository)
    }

    @Test
    fun `invoke should return updated task`() = runTest {
        // Given
        val projectId = 1L
        val taskId = 1L
        val name = "Updated Task"
        val description = "Updated description"
        val urgency = "URGENT"
        val complexity = "HARD"
        val status = "DONE"
        val expectedTask = Task(
            taskId,
            projectId,
            name,
            description,
            TaskStatus.DONE,
            TaskUrgency.URGENT,
            TaskComplexity.HARD,
            true,
            "2023-01-01",
            "2023-01-02",
            null,
            null
        )
        whenever(projectRepository.updateTask(projectId, taskId, name, urgency, complexity, status, description))
            .thenReturn(Result.success(expectedTask))

        // When
        val result = updateTaskUseCase(projectId, taskId, name, urgency, complexity, status, description)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }

    @Test
    fun `invoke should return error when name is empty`() = runTest {
        // Given
        val projectId = 1L
        val taskId = 1L
        val emptyName = ""

        // When
        val result = updateTaskUseCase(projectId, taskId, emptyName)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Task name cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when name is too long`() = runTest {
        // Given
        val projectId = 1L
        val taskId = 1L
        val longName = "a".repeat(201)

        // When
        val result = updateTaskUseCase(projectId, taskId, name = longName)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Task name cannot exceed 200 characters", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when description is too long`() = runTest {
        // Given
        val projectId = 1L
        val taskId = 1L
        val longDescription = "a".repeat(2001)

        // When
        val result = updateTaskUseCase(projectId, taskId, description = longDescription)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Task description cannot exceed 2000 characters", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val taskId = 1L
        val name = "Updated Task"
        val expectedError = Exception("Failed to update task")
        whenever(projectRepository.updateTask(projectId, taskId, name, null, null, null, null))
            .thenReturn(Result.failure(expectedError))

        // When
        val result = updateTaskUseCase(projectId, taskId, name)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}