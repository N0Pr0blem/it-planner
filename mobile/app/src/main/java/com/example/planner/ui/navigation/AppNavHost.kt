package com.example.planner.ui.navigation

import android.content.Context
import android.content.Intent
import android.content.ActivityNotFoundException
import android.net.Uri
import android.graphics.BitmapFactory
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.example.planner.domain.model.ProjectRole
import com.example.planner.domain.model.ProjectMember
import com.example.planner.ui.screens.*
import com.example.planner.ui.viewmodel.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavRoutes.Login.route
) {
    var imagePreview by remember { mutableStateOf<ImagePreview?>(null) }
    var selectedMember by remember { mutableStateOf<ProjectUserUi?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
        composable(NavRoutes.Login.route) {
            val viewModel: AuthViewModel = hiltViewModel()
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
            val viewModel: AuthViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(uiState.isRegistered) {
                if (uiState.isRegistered) {
                    viewModel.resetRegistrationState()
                    navController.navigate(NavRoutes.Verify.createRoute(uiState.lastRegisteredUsername))
                }
            }

            RegisterScreen(
                onTabSwitch = { tab ->
                    if (tab == "Login") {
                        navController.popBackStack()
                    }
                },
                onRegister = { email, name, password ->
                    viewModel.register(email, password, name)
                },
                isLoading = uiState.isLoading,
                error = uiState.error,
                onErrorDismiss = { viewModel.clearError() }
            )
        }

        composable(
            route = NavRoutes.Verify.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            val viewModel: AuthViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            LaunchedEffect(uiState.isVerified) {
                if (uiState.isVerified) {
                    viewModel.resetVerificationState()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                }
            }
            LaunchedEffect(uiState.error) {
                val message = uiState.error ?: return@LaunchedEffect
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }

            VerifyScreen(
                onVerify = { code ->
                    viewModel.verify(username, code)
                },
                onResend = {
                    viewModel.resendVerificationCode(username)
                },
                isLoading = uiState.isLoading
            )
        }

        composable(NavRoutes.Projects.route) {
            val viewModel: ProjectsViewModel = hiltViewModel()
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
            val viewModel: ProjectsViewModel = hiltViewModel()
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
            val viewModel: ProjectDetailsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val savedStateHandle = backStackEntry.savedStateHandle
            val tasksDirty = savedStateHandle
                ?.getStateFlow("project_tasks_dirty", false)
                ?.collectAsState()
                ?.value ?: false
            val membersDirty = savedStateHandle
                ?.getStateFlow("project_members_dirty", false)
                ?.collectAsState()
                ?.value ?: false
            val lifecycleOwner = LocalLifecycleOwner.current
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            var selectedRepoUri by remember { mutableStateOf<Uri?>(null) }
            var selectedRepoName by remember { mutableStateOf<String?>(null) }
            val pickRepoFileLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri ->
                if (uri == null) {
                    return@rememberLauncherForActivityResult
                }
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {
                }
                selectedRepoUri = uri
                selectedRepoName = getDisplayName(context, uri)
            }

            LaunchedEffect(projectId) {
                viewModel.loadProjectDetails(projectId)
            }
            LaunchedEffect(tasksDirty) {
                if (tasksDirty) {
                    viewModel.loadTasks(forceRefresh = true)
                    savedStateHandle?.set("project_tasks_dirty", false)
                }
            }
            LaunchedEffect(membersDirty) {
                if (membersDirty) {
                    viewModel.loadEmployees(forceRefresh = true)
                    savedStateHandle?.set("project_members_dirty", false)
                }
            }
            DisposableEffect(lifecycleOwner, projectId) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        viewModel.loadTasks(forceRefresh = true)
                        viewModel.loadEmployees(forceRefresh = true)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }
            LaunchedEffect(viewModel) {
                viewModel.repoEvents.collect { event ->
                    when (event) {
                        is RepoEvent.Downloaded -> {
                            if (isImageFile(event.filename)) {
                                imagePreview = ImagePreview(event.filename, event.bytes)
                            } else {
                                val file = writeBytesToCache(context, event.filename, event.bytes)
                                if (file != null) {
                                    openFile(context, file)
                                } else {
                                    Toast.makeText(context, "Failed to save file", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        is RepoEvent.Error -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
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
                onMemberClick = { member: ProjectMember ->
                    val roleText = member.role.name
                        .lowercase()
                        .split('_')
                        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
                    val name = listOfNotNull(
                        member.firstName?.takeIf { it.isNotBlank() },
                        member.secondName?.takeIf { it.isNotBlank() }
                    ).joinToString(" ").ifBlank { "User ${member.id}" }
                    selectedMember = ProjectUserUi(
                        id = member.id.toString(),
                        fullName = name,
                        roleInProject = roleText,
                        role = member.role,
                        firstName = member.firstName.orEmpty(),
                        lastName = member.secondName.orEmpty(),
                        username = null,
                        userId = null,
                        profileImageBase64 = member.profileImageUrl
                    )
                    navController.navigate(NavRoutes.TaskUserInfo.createRoute(projectId, member.id))
                },
                onTabChange = { tab ->
                    when (tab) {
                        ProjectTab.Tasks -> viewModel.loadTasks()
                        ProjectTab.Members -> viewModel.loadEmployees(forceRefresh = true)
                        ProjectTab.Repository -> viewModel.loadRepoFiles()
                    }
                },
                selectedRepoFilename = selectedRepoName,
                onRepoPickFile = {
                    pickRepoFileLauncher.launch(arrayOf("*/*"))
                },
                onRepoUploadFile = {
                    val uri = selectedRepoUri
                    if (uri == null) {
                        Toast.makeText(context, "Pick a file first", Toast.LENGTH_SHORT).show()
                        return@ProjectDetailsScreenWithData
                    }
                    coroutineScope.launch {
                        val displayName = selectedRepoName ?: getDisplayName(context, uri)
                        val file = copyUriToCache(context, uri, displayName)
                        if (file == null) {
                            Toast.makeText(context, "Failed to read file", Toast.LENGTH_SHORT).show()
                        } else {
                            val contentType = context.contentResolver.getType(uri)
                            viewModel.uploadRepoFile(file, displayName, contentType)
                        }
                    }
                },
                onRepoClearPickedFile = {
                    selectedRepoUri = null
                    selectedRepoName = null
                },
                onRepoFileClick = { file ->
                    val fileId = file.id.toLongOrNull() ?: return@ProjectDetailsScreenWithData
                    viewModel.downloadRepoFile(fileId, file.filename)
                },
                onRepoDeleteFile = { file ->
                    val fileId = file.id.toLongOrNull() ?: return@ProjectDetailsScreenWithData
                    viewModel.deleteRepoFile(fileId)
                },
                onErrorDismiss = { viewModel.clearError() }
            )
        }

        composable(
            route = NavRoutes.CreateTask.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val viewModel: CreateTaskViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(uiState.taskCreated) {
                if (uiState.taskCreated) {
                    viewModel.resetTaskCreated()
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("project_tasks_dirty", true)
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
            val viewModel: TaskDetailsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val pickTaskFileLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri ->
                if (uri == null) {
                    return@rememberLauncherForActivityResult
                }
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {
                }
                coroutineScope.launch {
                    val displayName = getDisplayName(context, uri)
                    val file = copyUriToCache(context, uri, displayName)
                    if (file == null) {
                        Toast.makeText(context, "Failed to read file", Toast.LENGTH_SHORT).show()
                    } else {
                        val contentType = context.contentResolver.getType(uri)
                        viewModel.uploadTaskFile(file, displayName, contentType)
                    }
                }
            }

            LaunchedEffect(projectId, taskId) {
                viewModel.loadTaskDetails(projectId, taskId)
            }
            LaunchedEffect(uiState.taskUpdated) {
                if (uiState.taskUpdated) {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("project_tasks_dirty", true)
                    viewModel.resetTaskUpdated()
                }
            }
            LaunchedEffect(viewModel) {
                viewModel.fileEvents.collect { event ->
                    when (event) {
                        is TaskFileEvent.Downloaded -> {
                            if (isImageFile(event.filename)) {
                                imagePreview = ImagePreview(event.filename, event.bytes)
                            } else {
                                val file = writeBytesToCache(context, event.filename, event.bytes)
                                if (file != null) {
                                    openFile(context, file)
                                } else {
                                    Toast.makeText(context, "Failed to save file", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        is TaskFileEvent.Error -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            TaskDetailsScreenWithData(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onOpenTimeAndAssignees = {
                    navController.navigate(NavRoutes.TimeAndAssignees.createRoute(projectId, taskId))
                },
                onEditStatus = { status ->
                    viewModel.updateStatus(status)
                },
                onEditTask = { title, description, priority, complexity ->
                    viewModel.updateTask(title, description, priority, complexity)
                },
                onDownloadFile = { file ->
                    val fileId = file.id.toLongOrNull() ?: return@TaskDetailsScreenWithData
                    viewModel.downloadTaskFile(fileId, file.fileName ?: "file_${file.id}")
                },
                onDeleteFile = { file ->
                    val fileId = file.id.toLongOrNull() ?: return@TaskDetailsScreenWithData
                    viewModel.deleteTaskFile(fileId)
                },
                onAddFile = {
                    pickTaskFileLauncher.launch(arrayOf("*/*"))
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
            val viewModel: TaskDetailsViewModel = hiltViewModel()
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
                },
                onAssignResponsible = { user ->
                    viewModel.assignResponsible(user)
                }
            )
        }

        composable(
            route = NavRoutes.InviteMember.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val viewModel: ProjectDetailsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(projectId) {
                viewModel.loadProjectDetails(projectId)
            }

            LaunchedEffect(uiState.employeeInvited) {
                if (uiState.employeeInvited) {
                    viewModel.resetEmployeeInvited()
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("project_members_dirty", true)
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
            val viewModel: ProfileViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            var selectedProfilePhotoFile by remember { mutableStateOf<File?>(null) }
            var selectedProfilePhotoBytes by remember { mutableStateOf<ByteArray?>(null) }
            val pickProfilePhotoLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri ->
                if (uri == null) {
                    return@rememberLauncherForActivityResult
                }
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {
                }
                val displayName = getDisplayName(context, uri)
                val file = copyUriToCache(context, uri, displayName)
                if (file != null) {
                    selectedProfilePhotoFile = file
                    selectedProfilePhotoBytes = runCatching { file.readBytes() }.getOrNull()
                } else {
                    Toast.makeText(context, "Failed to read photo", Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(Unit) {
                viewModel.loadProfile()
            }
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        viewModel.loadProfile()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }
            LaunchedEffect(uiState.error) {
                val message = uiState.error ?: return@LaunchedEffect
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
            LaunchedEffect(uiState.profileUpdated) {
                if (uiState.profileUpdated) {
                    selectedProfilePhotoFile = null
                    selectedProfilePhotoBytes = null
                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    viewModel.resetProfileUpdated()
                }
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
                selectedPhotoBytes = selectedProfilePhotoBytes,
                onPickPhoto = {
                    pickProfilePhotoLauncher.launch(arrayOf("image/*"))
                },
                onSaveProfile = { lastName, secondName ->
                    Toast.makeText(context, "Saving profile...", Toast.LENGTH_SHORT).show()
                    viewModel.updateProfile(secondName, lastName, selectedProfilePhotoFile)
                },
                onCancelEdit = {
                    selectedProfilePhotoFile = null
                    selectedProfilePhotoBytes = null
                }
            )
        }

        composable(
            route = NavRoutes.TaskUserInfo.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType },
                navArgument("employeeId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val employeeId = backStackEntry.arguments?.getLong("employeeId") ?: return@composable
            val viewModel: ProjectDetailsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val profileState by profileViewModel.uiState.collectAsState()
            val context = LocalContext.current

            LaunchedEffect(projectId) {
                viewModel.loadProjectDetails(projectId)
                viewModel.loadEmployees(projectId)
            }
            LaunchedEffect(Unit) {
                profileViewModel.loadProfile()
            }
            LaunchedEffect(uiState.memberRoleUpdated) {
                if (uiState.memberRoleUpdated) {
                    Toast.makeText(context, "Role updated", Toast.LENGTH_SHORT).show()
                    viewModel.resetMemberRoleUpdated()
                }
            }

            val member = selectedMember ?: run {
                val fallback = uiState.employees.firstOrNull { it.id == employeeId }
                if (fallback != null) {
                    val name = listOfNotNull(
                        fallback.firstName?.takeIf { it.isNotBlank() },
                        fallback.secondName?.takeIf { it.isNotBlank() }
                    ).joinToString(" ").ifBlank { "User $employeeId" }
                    ProjectUserUi(
                        id = employeeId.toString(),
                        fullName = name,
                        roleInProject = fallback.role.name
                            .lowercase()
                            .split('_')
                            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } },
                        role = fallback.role,
                        firstName = fallback.firstName ?: "",
                        lastName = fallback.secondName ?: "",
                        username = null,
                        userId = null,
                        profileImageBase64 = fallback.profileImageUrl
                    )
                } else {
                    ProjectUserUi(
                        id = employeeId.toString(),
                        fullName = "User $employeeId",
                        roleInProject = "",
                        role = ProjectRole.ANOTHER,
                        firstName = "",
                        lastName = "",
                        username = null,
                        userId = null,
                        profileImageBase64 = null
                    )
                }
            }
            val resolvedMember = run {
                val profile = profileState.profile
                val lastName = profile?.lastName?.takeIf { it.isNotBlank() } ?: member.lastName
                val firstName = member.firstName.ifBlank { profile?.firstName?.orEmpty() ?: "" }
                val profileImage = member.profileImageBase64 ?: profile?.profileImage
                member.copy(
                    firstName = firstName,
                    lastName = lastName,
                    profileImageBase64 = profileImage
                )
            }

            ProjectUserDetailsScreen(
                user = resolvedMember,
                onBack = { navController.popBackStack() },
                onKickFromProject = {
                    viewModel.deleteEmployee(employeeId)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("project_members_dirty", true)
                    navController.popBackStack()
                },
                onSaveRole = { role ->
                    viewModel.updateEmployeeRole(employeeId, role)
                    selectedMember = selectedMember?.copy(
                        role = role,
                        roleInProject = role.name
                            .lowercase()
                            .split('_')
                            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
                    )
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("project_members_dirty", true)
                }
            )
        }
    }

        val preview = imagePreview
        if (preview != null) {
            Dialog(onDismissRequest = { imagePreview = null }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    val bitmap = remember(preview) {
                        BitmapFactory.decodeByteArray(preview.bytes, 0, preview.bytes.size)
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = preview.name)
                        Spacer(Modifier.height(12.dp))
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = preview.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .sizeIn(maxHeight = 520.dp),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Text(text = "Failed to decode image")
                        }
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { imagePreview = null }) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}

private fun roleFromString(role: String): com.example.planner.domain.model.ProjectRole {
    return when (role) {
        "Backend Developer" -> com.example.planner.domain.model.ProjectRole.BACKEND_DEVELOPER
        "Frontend Developer" -> com.example.planner.domain.model.ProjectRole.FRONTEND_DEVELOPER
        "Designer", "UI/UX designer" -> com.example.planner.domain.model.ProjectRole.UI_UX_DESIGNER
        "QA Engineer", "Tester" -> com.example.planner.domain.model.ProjectRole.TESTER
        "Project Manager" -> com.example.planner.domain.model.ProjectRole.PROJECT_MANAGER
        "DevOps" -> com.example.planner.domain.model.ProjectRole.DEVOPS
        else -> com.example.planner.domain.model.ProjectRole.ANOTHER
    }
}

private data class ImagePreview(val name: String, val bytes: ByteArray)

private fun isImageFile(filename: String): Boolean {
    val ext = filename.substringAfterLast('.', "").lowercase()
    return ext in setOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
}

private fun getDisplayName(context: Context, uri: Uri): String? {
    val resolver = context.contentResolver
    resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && cursor.moveToFirst()) {
            return cursor.getString(nameIndex)
        }
    }
    return uri.lastPathSegment
}

private fun sanitizeFileName(name: String): String {
    return name.replace(Regex("[\\\\/:*?\"<>|]"), "_")
}

private fun copyUriToCache(context: Context, uri: Uri, displayName: String?): File? {
    val mimeType = context.contentResolver.getType(uri)
    val extension = if (!mimeType.isNullOrBlank()) {
        MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    } else {
        null
    }
    val baseName = displayName ?: "upload_${System.currentTimeMillis()}"
    val safeName = if (!extension.isNullOrBlank() && !baseName.contains('.')) {
        sanitizeFileName("$baseName.$extension")
    } else {
        sanitizeFileName(baseName)
    }
    val outFile = File(context.cacheDir, safeName)
    return try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(outFile).use { output ->
                input.copyTo(output)
            }
        } ?: return null
        outFile
    } catch (_: IOException) {
        null
    }
}

private fun writeBytesToCache(context: Context, filename: String, bytes: ByteArray): File? {
    val safeName = sanitizeFileName(filename.ifBlank { "download_${System.currentTimeMillis()}" })
    val outFile = File(context.cacheDir, safeName)
    return try {
        FileOutputStream(outFile).use { output ->
            output.write(bytes)
        }
        outFile
    } catch (_: IOException) {
        null
    }
}

private fun openFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val extension = file.extension.lowercase()
    val guessedType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    val mimeType = context.contentResolver.getType(uri) ?: guessedType ?: "*/*"
    val viewIntent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val chooser = Intent.createChooser(viewIntent, "Open with")
    try {
        context.startActivity(chooser)
    } catch (_: ActivityNotFoundException) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        try {
            context.startActivity(Intent.createChooser(shareIntent, "Share file"))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "No app available to open this file", Toast.LENGTH_SHORT).show()
        }
    }
}
