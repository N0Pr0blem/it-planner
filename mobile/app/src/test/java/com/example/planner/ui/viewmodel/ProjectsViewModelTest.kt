package com.example.planner.ui.viewmodel

import com.example.planner.domain.model.Project
import com.example.planner.domain.usecase.CreateProjectUseCase
import com.example.planner.domain.usecase.DeleteProjectUseCase
import com.example.planner.domain.usecase.GetProjectsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val getProjects: GetProjectsUseCase = mock()
    private val createProject: CreateProjectUseCase = mock()
    private val deleteProject: DeleteProjectUseCase = mock()

    private lateinit var viewModel: ProjectsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = ProjectsViewModel(getProjects, createProject, deleteProject)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProjects populates state on success`() = runTest(dispatcher) {
        val projects = listOf(Project(1, "Test", "", ""))
        whenever(getProjects()).thenReturn(Result.success(projects))

        viewModel.loadProjects()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals(1, state.projects.size)
        assertEquals("Test", state.projects.first().name)
    }

    @Test
    fun `loadProjects sets error on failure`() = runTest(dispatcher) {
        whenever(getProjects()).thenReturn(Result.failure(RuntimeException("fail")))

        viewModel.loadProjects()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.error?.contains("fail") == true)
    }

    @Test
    fun `createProject updates state on success`() = runTest(dispatcher) {
        whenever(createProject(any())).thenReturn(Result.success(Project(1, "New", "", "")))
        whenever(getProjects()).thenReturn(Result.success(emptyList()))

        viewModel.createProject("New")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.projectCreated)
        assertEquals(false, state.isLoading)
        verify(createProject).invoke("New")
    }

    @Test
    fun `deleteProject sets error on failure`() = runTest(dispatcher) {
        whenever(deleteProject(1)).thenReturn(Result.failure(RuntimeException("boom")))

        viewModel.deleteProject(1)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.error?.contains("boom") == true)
    }
}
