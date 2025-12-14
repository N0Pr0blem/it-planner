package com.example.planner.domain.usecase

import com.example.planner.domain.repository.ProjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DeleteEmployeeUseCaseTest {

    private lateinit var deleteEmployeeUseCase: DeleteEmployeeUseCase
    private val projectRepository: ProjectRepository = mock()

    @Before
    fun setUp() {
        deleteEmployeeUseCase = DeleteEmployeeUseCase(projectRepository)
    }

    @Test
    fun `invoke should return success when employee is deleted`() = runTest {
        // Given
        val projectId = 1L
        val employeeId = 1L
        whenever(projectRepository.deleteEmployee(projectId, employeeId)).thenReturn(Result.success(Unit))

        // When
        val result = deleteEmployeeUseCase(projectId, employeeId)

        // Then
        assert(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val projectId = 1L
        val employeeId = 1L
        val expectedError = Exception("Failed to delete employee")
        whenever(projectRepository.deleteEmployee(projectId, employeeId)).thenReturn(Result.failure(expectedError))

        // When
        val result = deleteEmployeeUseCase(projectId, employeeId)

        // Then
        assert(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}