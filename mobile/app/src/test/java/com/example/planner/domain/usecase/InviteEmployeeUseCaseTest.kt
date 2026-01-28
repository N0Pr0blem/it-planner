package com.example.planner.domain.usecase

import com.example.planner.domain.exception.ValidationException
import com.example.planner.domain.model.ProjectRole
import com.example.planner.domain.model.ProjectMember
import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class InviteEmployeeUseCaseTest {

    private lateinit var inviteEmployeeUseCase: InviteEmployeeUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        inviteEmployeeUseCase = InviteEmployeeUseCase(projectRepository)
    }

    @Test
    fun `invoke should return invited user`() = runTest {
        // Given
        val projectId = 1L
        val username = "testuser"
        val role = ProjectRole.BACKEND_DEVELOPER
        val expectedUser = ProjectMember(1, ProjectRole.BACKEND_DEVELOPER, "Test", "User", "profile.jpg")
        whenever(projectRepository.inviteEmployee(projectId, username, role))
            .thenReturn(Result.success(expectedUser))

        // When
        val result = inviteEmployeeUseCase(projectId, username, role)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
    }

    @Test
    fun `invoke should return error when username is empty`() = runTest {
        // Given
        val projectId = 1L
        val emptyUsername = ""
        val role = ProjectRole.BACKEND_DEVELOPER

        // When
        val result = inviteEmployeeUseCase(projectId, emptyUsername, role)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Username cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when username is too long`() = runTest {
        // Given
        val projectId = 1L
        val longUsername = "a".repeat(51)
        val role = ProjectRole.BACKEND_DEVELOPER

        // When
        val result = inviteEmployeeUseCase(projectId, longUsername, role)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Username cannot exceed 50 characters", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val username = "testuser"
        val role = ProjectRole.BACKEND_DEVELOPER
        val expectedError = Exception("Failed to invite employee")
        whenever(projectRepository.inviteEmployee(projectId, username, role))
            .thenReturn(Result.failure(expectedError))

        // When
        val result = inviteEmployeeUseCase(projectId, username, role)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}