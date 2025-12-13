package com.example.planner.domain.usecase

import com.example.planner.domain.model.Project
import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CreateProjectUseCaseTest {

    private lateinit var createProjectUseCase: CreateProjectUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        createProjectUseCase = CreateProjectUseCase(projectRepository)
    }

    @Test
    fun `invoke should return created project`() = runTest {
        // Given
        val projectName = "New Project"
        val expectedProject = Project(1, projectName, "2023-01-01", "2023-01-02")
        whenever(projectRepository.createProject(projectName)).thenReturn(Result.success(expectedProject))

        // When
        val result = createProjectUseCase(projectName)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedProject, result.getOrNull())
    }

    @Test
    fun `invoke should return error when name is empty`() = runTest {
        // Given
        val emptyName = ""

        // When
        val result = createProjectUseCase(emptyName)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Project name cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectName = "New Project"
        val expectedError = Exception("Failed to create project")
        whenever(projectRepository.createProject(projectName)).thenReturn(Result.failure(expectedError))

        // When
        val result = createProjectUseCase(projectName)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}