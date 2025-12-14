package com.example.planner.domain.usecase

import com.example.planner.domain.model.Task
import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetTaskUseCaseTest {

    private lateinit var getTaskUseCase: GetTaskUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        getTaskUseCase = GetTaskUseCase(projectRepository)
    }

    @Test
    fun `invoke should return task`() = runTest {
        // Given
        val projectId = 1L
        val taskId = 1L
        val expectedTask = Task(taskId, projectId, "Task 1", "Description 1", "2023-01-01", "2023-01-02")
        whenever(projectRepository.getTask(projectId, taskId)).thenReturn(Result.success(expectedTask))

        // When
        val result = getTaskUseCase(projectId, taskId)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val taskId = 1L
        val expectedError = Exception("Failed to load task")
        whenever(projectRepository.getTask(projectId, taskId)).thenReturn(Result.failure(expectedError))

        // When
        val result = getTaskUseCase(projectId, taskId)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}