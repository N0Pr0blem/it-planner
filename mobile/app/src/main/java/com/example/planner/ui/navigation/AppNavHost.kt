package com.example.planner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.planner.ui.screens.*
import com.example.planner.ui.viewmodel.*

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavRoutes.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.Login.route) {
            val viewModel: AuthViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(uiState.isLoggedIn) {
                if (uiState.isLoggedIn) {
                    navController.navigate(NavRoutes.Projects.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                onTabSwitch = { tab ->
                    if (tab == "Register") {
                        navController.navigate(NavRoutes.Register.route)
                    }
                },
                onLogin = { username, password ->
                    viewModel.login(username, password)
                },
                isLoading = uiState.isLoading,
                error = uiState.error,
                onErrorDismiss = { viewModel.clearError() }
            )
        }

        composable(NavRoutes.Register.route) {
            val viewModel: AuthViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(uiState.isRegistered) {
                if (uiState.isRegistered) {
                    viewModel.resetRegistrationState()
                    navController.navigate(NavRoutes.Verify.route)
                }
            }

            RegisterScreen(
                onTabSwitch = { tab ->
                    if (tab == "Login") {
                        navController.popBackStack()
                    }
                },
                onRegister = { username, email, password ->
                    viewModel.register(username, password, email)
                },
                isLoading = uiState.isLoading,
                error = uiState.error,
                onErrorDismiss = { viewModel.clearError() }
            )
        }

        composable(NavRoutes.Verify.route) {
            VerifyScreen(
                onVerify = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Projects.route) {
            val viewModel: ProjectsViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadProjects()
            }

            ProjectsScreenWithData(
                uiState = uiState,
                onAddProject = {
                    navController.navigate(NavRoutes.CreateProject.route)
                },
                onProjectClick = { project ->
                    navController.navigate(NavRoutes.ProjectDetails.createRoute(project.id.toLong()))
                },
                onDeleteProject = { project ->
                    viewModel.deleteProject(project.id.toLong())
                },
                onAccountClick = {
                    navController.navigate(NavRoutes.PersonalAccount.route)
                }
            )
        }

        composable(NavRoutes.CreateProject.route) {
            val viewModel: ProjectsViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(uiState.projectCreated) {
                if (uiState.projectCreated) {
                    viewModel.resetProjectCreated()
                    navController.popBackStack()
                }
            }

            CreateProjectScreen(
                onBack = { navController.popBackStack() },
                onCreate = { name ->
                    viewModel.createProject(name)
                },
                isLoading = uiState.isLoading,
                error = uiState.error
            )
        }

        composable(
            route = NavRoutes.ProjectDetails.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val viewModel: ProjectDetailsViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(projectId) {
                viewModel.loadProjectDetails(projectId)
            }

            ProjectDetailsScreenWithData(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onAddMember = {
                    navController.navigate(NavRoutes.InviteMember.createRoute(projectId))
                },
                onAddTask = {
                    navController.navigate(NavRoutes.CreateTask.createRoute(projectId))
                },
                onTaskClick = { task ->
                    navController.navigate(NavRoutes.TaskDetails.createRoute(projectId, task.id.toLong()))
                },
                onTabChange = { tab ->
                    when (tab) {
                        ProjectTab.Tasks -> viewModel.loadTasks()
                        ProjectTab.Members -> viewModel.loadEmployees()
                        ProjectTab.Repository -> viewModel.loadRepoFiles()
                    }
                }
            )
        }

        composable(
            route = NavRoutes.CreateTask.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val viewModel: CreateTaskViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(uiState.taskCreated) {
                if (uiState.taskCreated) {
                    viewModel.resetTaskCreated()
                    navController.popBackStack()
                }
            }

            CreateTaskScreen(
                onBack = { navController.popBackStack() },
                onCreate = { title, description, priority, complexity ->
                    viewModel.createTask(projectId, title, description, priority, complexity)
                }
            )
        }

        composable(
            route = NavRoutes.TaskDetails.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType },
                navArgument("taskId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: return@composable
            val viewModel: TaskDetailsViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(projectId, taskId) {
                viewModel.loadTaskDetails(projectId, taskId)
            }

            TaskDetailsScreenWithData(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onOpenTimeAndAssignees = {
                    navController.navigate(NavRoutes.TimeAndAssignees.createRoute(projectId, taskId))
                },
                onEditStatus = { status ->
                    viewModel.updateStatus(status)
                }
            )
        }

        composable(
            route = NavRoutes.TimeAndAssignees.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType },
                navArgument("taskId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: return@composable
            val viewModel: TaskDetailsViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(projectId, taskId) {
                viewModel.loadTaskDetails(projectId, taskId)
            }

            TimeAndAssigneesScreenWithData(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onAddTimeRecord = { hours, _ ->
                    viewModel.addTracking(hours.toDoubleOrNull() ?: 0.0)
                },
                onDeleteEntry = { entry ->
                    viewModel.deleteTracking(entry.id.toLong())
                }
            )
        }

        composable(
            route = NavRoutes.InviteMember.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val viewModel: ProjectDetailsViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(projectId) {
                viewModel.loadProjectDetails(projectId)
            }

            LaunchedEffect(uiState.employeeInvited) {
                if (uiState.employeeInvited) {
                    viewModel.resetEmployeeInvited()
                    navController.popBackStack()
                }
            }

            InviteMemberScreenWithData(
                projectName = uiState.projectName,
                onBack = { navController.popBackStack() },
                onInvite = { email, role ->
                    viewModel.inviteEmployee(email, roleFromString(role))
                },
                isLoading = uiState.isLoading,
                error = uiState.error
            )
        }

        composable(NavRoutes.PersonalAccount.route) {
            val viewModel: ProfileViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadProfile()
            }

            PersonalAccountScreenWithData(
                uiState = uiState,
                onProjectsClick = { navController.popBackStack() },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSaveProfile = { login, password ->
                    viewModel.updateProfile(login, null, null)
                }
            )
        }
    }
}

private fun roleFromString(role: String): com.example.planner.data.model.user.ProjectRole {
    return when (role) {
        "Backend Developer" -> com.example.planner.data.model.user.ProjectRole.BACKEND_DEVELOPER
        "Frontend Developer" -> com.example.planner.data.model.user.ProjectRole.FRONTEND_DEVELOPER
        "Designer", "UI/UX designer" -> com.example.planner.data.model.user.ProjectRole.UI_UX_DESIGNER
        "QA Engineer", "Tester" -> com.example.planner.data.model.user.ProjectRole.TESTER
        "Project Manager" -> com.example.planner.data.model.user.ProjectRole.PROJECT_MANAGER
        "DevOps" -> com.example.planner.data.model.user.ProjectRole.DEVOPS
        else -> com.example.planner.data.model.user.ProjectRole.ANOTHER
    }
}
