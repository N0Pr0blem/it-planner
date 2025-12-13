package com.example.planner.domain.usecase

import com.example.planner.domain.model.Project
import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetProjectsUseCaseTest {

    private lateinit var getProjectsUseCase: GetProjectsUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        getProjectsUseCase = GetProjectsUseCase(projectRepository)
    }

    @Test
    fun `invoke should return list of projects`() = runTest {
        // Given
        val expectedProjects = listOf(
            Project(1, "Project 1", "2023-01-01", "2023-01-02"),
            Project(2, "Project 2", "2023-01-03", "2023-01-04")
        )
        whenever(projectRepository.getProjects()).thenReturn(Result.success(expectedProjects))

        // When
        val result = getProjectsUseCase()

        // Then
        assert(result.isSuccess)
        assertEquals(expectedProjects, result.getOrNull())
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = Exception("Failed to load projects")
        whenever(projectRepository.getProjects()).thenReturn(Result.failure(expectedError))

        // When
        val result = getProjectsUseCase()

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}