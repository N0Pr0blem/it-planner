package com.example.planner.domain.usecase

import com.example.planner.domain.model.ProjectMember
import com.example.planner.domain.model.ProjectRole
import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetProjectEmployeesUseCaseTest {

    private lateinit var getProjectEmployeesUseCase: GetProjectEmployeesUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        getProjectEmployeesUseCase = GetProjectEmployeesUseCase(projectRepository)
    }

    @Test
    fun `invoke should return list of employees for project`() = runTest {
        // Given
        val projectId = 1L
        val expectedEmployees = listOf(
            ProjectMember(1, ProjectRole.BACKEND_DEVELOPER, "John", "Doe", "profile1.jpg"),
            ProjectMember(2, ProjectRole.TESTER, "Jane", "Smith", "profile2.jpg")
        )
        whenever(projectRepository.getProjectEmployees(projectId)).thenReturn(Result.success(expectedEmployees))

        // When
        val result = getProjectEmployeesUseCase(projectId)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedEmployees, result.getOrNull())
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val expectedError = Exception("Failed to load project employees")
        whenever(projectRepository.getProjectEmployees(projectId)).thenReturn(Result.failure(expectedError))

        // When
        val result = getProjectEmployeesUseCase(projectId)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}