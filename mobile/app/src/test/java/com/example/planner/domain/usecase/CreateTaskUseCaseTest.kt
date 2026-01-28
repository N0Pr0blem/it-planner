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

class CreateTaskUseCaseTest {

    private lateinit var createTaskUseCase: CreateTaskUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        createTaskUseCase = CreateTaskUseCase(projectRepository)
    }

    @Test
    fun `invoke should return created task`() = runTest {
        // Given
        val projectId = 1L
        val name = "New Task"
        val description = "Task description"
        val urgency = "MEDIUM"
        val complexity = "MEDIUM"
        val expectedTask = Task(
            1,
            projectId,
            name,
            description,
            TaskStatus.TO_DO,
            TaskUrgency.MEDIUM,
            TaskComplexity.MEDIUM,
            false,
            "2023-01-01",
            "2023-01-02",
            null,
            null
        )
        whenever(projectRepository.createTask(projectId, name, description, urgency, complexity))
            .thenReturn(Result.success(expectedTask))

        // When
        val result = createTaskUseCase(projectId, name, description, urgency, complexity)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }

    @Test
    fun `invoke should return error when name is empty`() = runTest {
        // Given
        val projectId = 1L
        val emptyName = ""
        val description = "Task description"
        val urgency = "MEDIUM"
        val complexity = "MEDIUM"

        // When
        val result = createTaskUseCase(projectId, emptyName, description, urgency, complexity)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Task name cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when name is too long`() = runTest {
        // Given
        val projectId = 1L
        val longName = "a".repeat(201)
        val description = "Task description"
        val urgency = "MEDIUM"
        val complexity = "MEDIUM"

        // When
        val result = createTaskUseCase(projectId, longName, description, urgency, complexity)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Task name cannot exceed 200 characters", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when description is too long`() = runTest {
        // Given
        val projectId = 1L
        val name = "New Task"
        val longDescription = "a".repeat(2001)
        val urgency = "MEDIUM"
        val complexity = "MEDIUM"

        // When
        val result = createTaskUseCase(projectId, name, longDescription, urgency, complexity)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Task description cannot exceed 2000 characters", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val name = "New Task"
        val description = "Task description"
        val urgency = "MEDIUM"
        val complexity = "MEDIUM"
        val expectedError = Exception("Failed to create task")
        whenever(projectRepository.createTask(projectId, name, description, urgency, complexity))
            .thenReturn(Result.failure(expectedError))

        // When
        val result = createTaskUseCase(projectId, name, description, urgency, complexity)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}