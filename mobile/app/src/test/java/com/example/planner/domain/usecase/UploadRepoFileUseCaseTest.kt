package com.example.planner.domain.usecase

import com.example.planner.domain.repository.RepoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.File

class UploadRepoFileUseCaseTest {

    private val repoRepository: RepoRepository = mock()
    private lateinit var useCase: UploadRepoFileUseCase

    @Before
    fun setUp() {
        useCase = UploadRepoFileUseCase(repoRepository)
    }

    @Test
    fun `delegates to repository with same args`() = runTest {
        val file = File("dummy.txt")
        whenever(repoRepository.uploadRepoFile(1, file, "dummy.txt", "text/plain"))
            .thenReturn(Result.success("ok"))

        val result = useCase(1, file, "dummy.txt", "text/plain")

        assertTrue(result.isSuccess)
        assertEquals("ok", result.getOrNull())
        verify(repoRepository).uploadRepoFile(1, file, "dummy.txt", "text/plain")
    }
}
