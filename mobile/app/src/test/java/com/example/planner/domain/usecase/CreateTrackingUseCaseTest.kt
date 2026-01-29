package com.example.planner.domain.usecase

import com.example.planner.domain.model.TrackingRecord
import com.example.planner.domain.repository.TaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.any
import org.mockito.kotlin.capture
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

class CreateTrackingUseCaseTest {

    private val taskRepository: TaskRepository = mock()
    private lateinit var useCase: CreateTrackingUseCase

    @Before
    fun setUp() {
        useCase = CreateTrackingUseCase(taskRepository)
    }

    @Test
    fun `default date is today`() = runTest {
        val today = LocalDate.now()
        val record = TrackingRecord(1, today, 2.5, "John", "Doe", 10)
        val dateCaptor = ArgumentCaptor.forClass(LocalDate::class.java)
        whenever(
            taskRepository.createTracking(
                any(),
                any(),
                any(),
                capture(dateCaptor)
            )
        ).thenReturn(Result.success(record))

        val result = useCase(5, 9, 2.5)

        assertTrue(result.isSuccess)
        assertEquals(today, dateCaptor.value)
    }
}
