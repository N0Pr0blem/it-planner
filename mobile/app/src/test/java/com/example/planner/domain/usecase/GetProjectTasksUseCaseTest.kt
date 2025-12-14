package com.example.planner.domain.usecase

import com.example.planner.domain.model.Task
import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetProjectTasksUseCaseTest {

    private lateinit var getProjectTasksUseCase: GetProjectTasksUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        getProjectTasksUseCase = GetProjectTasksUseCase(projectRepository)
    }

    @Test
    fun `invoke should return list of tasks for project`() = runTest {
        // Given
        val projectId = 1L
        val expectedTasks = listOf(
            Task(1, projectId, "Task 1", "Description 1", "2023-01-01", "2023-01-02"),
            Task(2, projectId, "Task 2", "Description 2", "2023-01-03", "2023-01-04")
        )
        whenever(projectRepository.getProjectTasks(projectId)).thenReturn(Result.success(expectedTasks))

        // When
        val result = getProjectTasksUseCase(projectId)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedTasks, result.getOrNull())
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val expectedError = Exception("Failed to load project tasks")
        whenever(projectRepository.getProjectTasks(projectId)).thenReturn(Result.failure(expectedError))

        // When
        val result = getProjectTasksUseCase(projectId)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}