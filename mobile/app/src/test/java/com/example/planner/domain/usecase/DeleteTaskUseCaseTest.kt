package com.example.planner.domain.usecase

import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DeleteTaskUseCaseTest {

    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        deleteTaskUseCase = DeleteTaskUseCase(projectRepository)
    }

    @Test
    fun `invoke should return success when task is deleted`() = runTest {
        // Given
        val projectId = 1L
        val taskId = 1L
        whenever(projectRepository.deleteTask(projectId, taskId)).thenReturn(Result.success(Unit))

        // When
        val result = deleteTaskUseCase(projectId, taskId)

        // Then
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val taskId = 1L
        val expectedError = Exception("Failed to delete task")
        whenever(projectRepository.deleteTask(projectId, taskId)).thenReturn(Result.failure(expectedError))

        // When
        val result = deleteTaskUseCase(projectId, taskId)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}