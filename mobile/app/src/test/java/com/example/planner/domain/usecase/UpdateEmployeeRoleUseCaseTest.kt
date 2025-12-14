package com.example.planner.domain.usecase

import com.example.planner.domain.exception.ValidationException
import com.example.planner.domain.model.User
import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UpdateEmployeeRoleUseCaseTest {

    private lateinit var updateEmployeeRoleUseCase: UpdateEmployeeRoleUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        updateEmployeeRoleUseCase = UpdateEmployeeRoleUseCase(projectRepository)
    }

    @Test
    fun `invoke should return updated user`() = runTest {
        // Given
        val projectId = 1L
        val employeeId = 1L
        val role = "ADMIN"
        val expectedUser = User(employeeId, "testuser", "Test", "User", "test@example.com", "profile.jpg")
        whenever(projectRepository.updateEmployeeRole(projectId, employeeId, role))
            .thenReturn(Result.success(expectedUser))

        // When
        val result = updateEmployeeRoleUseCase(projectId, employeeId, role)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
    }

    @Test
    fun `invoke should return error when role is empty`() = runTest {
        // Given
        val projectId = 1L
        val employeeId = 1L
        val emptyRole = ""

        // When
        val result = updateEmployeeRoleUseCase(projectId, employeeId, emptyRole)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Role cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when role is too long`() = runTest {
        // Given
        val projectId = 1L
        val employeeId = 1L
        val longRole = "a".repeat(51)

        // When
        val result = updateEmployeeRoleUseCase(projectId, employeeId, longRole)

        // Then
        assert(result.isFailure)
        assertTrue(result.exceptionOrNull() is ValidationException)
        assertEquals("Role cannot exceed 50 characters", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val employeeId = 1L
        val role = "ADMIN"
        val expectedError = Exception("Failed to update employee role")
        whenever(projectRepository.updateEmployeeRole(projectId, employeeId, role))
            .thenReturn(Result.failure(expectedError))

        // When
        val result = updateEmployeeRoleUseCase(projectId, employeeId, role)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}