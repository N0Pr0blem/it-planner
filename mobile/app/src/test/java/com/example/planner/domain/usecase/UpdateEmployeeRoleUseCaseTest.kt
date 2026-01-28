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
        val role = ProjectRole.PROJECT_MANAGER
        val expectedUser = ProjectMember(employeeId, ProjectRole.PROJECT_MANAGER, "Test", "User", "profile.jpg")
        whenever(projectRepository.updateEmployeeRole(projectId, employeeId, role))
            .thenReturn(Result.success(expectedUser))

        // When
        val result = updateEmployeeRoleUseCase(projectId, employeeId, role)

        // Then
        assert(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val employeeId = 1L
        val role = ProjectRole.PROJECT_MANAGER
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
