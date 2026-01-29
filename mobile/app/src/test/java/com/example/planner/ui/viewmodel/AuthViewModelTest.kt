package com.example.planner.ui.viewmodel

import com.example.planner.domain.model.AuthSession
import com.example.planner.domain.model.RegisterInfo
import com.example.planner.domain.model.SimpleMessage
import com.example.planner.domain.usecase.GetCurrentUserIdUseCase
import com.example.planner.domain.usecase.IsLoggedInUseCase
import com.example.planner.domain.usecase.LoginUseCase
import com.example.planner.domain.usecase.LogoutUseCase
import com.example.planner.domain.usecase.RegisterUseCase
import com.example.planner.domain.usecase.ResendVerificationUseCase
import com.example.planner.domain.usecase.VerifyUseCase
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
import org.mockito.kotlin.whenever
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val login: LoginUseCase = mock()
    private val register: RegisterUseCase = mock()
    private val verify: VerifyUseCase = mock()
    private val resend: ResendVerificationUseCase = mock()
    private val logout: LogoutUseCase = mock()
    private val isLoggedIn: IsLoggedInUseCase = mock()
    private val getCurrentUserId: GetCurrentUserIdUseCase = mock()

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        whenever(isLoggedIn()).thenReturn(false)
        whenever(getCurrentUserId()).thenReturn(null)
        viewModel = AuthViewModel(login, register, verify, resend, logout, isLoggedIn, getCurrentUserId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success flips isLoggedIn`() = runTest(dispatcher) {
        whenever(login(any(), any())).thenReturn(
            Result.success(AuthSession("tkn", Date(), Date()))
        )
        whenever(getCurrentUserId()).thenReturn(42L)

        viewModel.login("u", "p")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isLoggedIn)
        assertEquals(42L, state.currentUserId)
    }

    @Test
    fun `register success sets isRegistered and last username`() = runTest(dispatcher) {
        whenever(register(any(), any(), any())).thenReturn(
            Result.success(RegisterInfo("u", "ok"))
        )

        viewModel.register("u@example.com", "pwd", "John")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isRegistered)
        assertEquals("u@example.com", state.lastRegisteredUsername)
    }

    @Test
    fun `verify failure surfaces error`() = runTest(dispatcher) {
        whenever(verify(any(), any())).thenReturn(Result.failure(Exception("wrong code")))

        viewModel.verify("u@example.com", "1234")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.error?.contains("wrong code") == true)
    }

    @Test
    fun `logout resets state`() = runTest(dispatcher) {
        whenever(login(any(), any())).thenReturn(
            Result.success(AuthSession("tkn", Date(), Date()))
        )
        viewModel.login("u", "p")
        advanceUntilIdle()

        viewModel.logout()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoggedIn)
        assertEquals(null, state.currentUserId)
    }
}
