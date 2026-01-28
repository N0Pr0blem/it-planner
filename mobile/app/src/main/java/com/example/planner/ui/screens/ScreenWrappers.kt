package com.example.planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planner.domain.model.TaskComplexity
import com.example.planner.domain.model.TaskStatus
import com.example.planner.domain.model.TaskUrgency
import com.example.planner.domain.model.ProjectMember
import com.example.planner.ui.extensions.title
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.viewmodel.*
import java.time.format.DateTimeFormatter

@Composable
fun ProjectsScreenWithData(
    uiState: ProjectsUiState,
    onAddProject: () -> Unit,
    onProjectClick: (ProjectUi) -> Unit,
    onDeleteProject: (ProjectUi) -> Unit,
    onAccountClick: () -> Unit
) {
    ProjectsScreen(
        projects = uiState.projects,
        selectedProjectId = null,
        isLoading = uiState.isLoading,
        onAddProject = onAddProject,
        onProjectClick = onProjectClick,
        onProjectLongClick = { project -> onDeleteProject(project) },
        onDeleteProject = onDeleteProject,
        onAccountClick = onAccountClick
    )
}

@Composable
fun ProjectDetailsScreenWithData(
    uiState: ProjectDetailsUiState,
    onBack: () -> Unit,
    onAddMember: () -> Unit,
    onAddTask: () -> Unit,
    onTaskClick: (ProjectTaskUi) -> Unit,
    onMemberClick: (ProjectMember) -> Unit,
    onTabChange: (ProjectTab) -> Unit,
    selectedRepoFilename: String?,
    onRepoPickFile: () -> Unit,
    onRepoUploadFile: () -> Unit,
    onRepoClearPickedFile: () -> Unit,
    onRepoFileClick: (RepoFileUi) -> Unit,
    onRepoDeleteFile: (RepoFileUi) -> Unit,
    onErrorDismiss: () -> Unit
) {
    var activeTab by remember { mutableStateOf(ProjectTab.Tasks) }

    ProjectDetailsScreen(
        projectName = uiState.projectName,
        tasks = uiState.tasks,
        employees = uiState.employees,
        repoFiles = uiState.repoFiles,
        activeTab = activeTab,
        onTabChange = { tab ->
            activeTab = tab
            onTabChange(tab)
        },
        onBack = onBack,
        onAddMember = onAddMember,
        onAddTask = onAddTask,
        onTaskClick = onTaskClick,
        onMemberClick = onMemberClick,
        onFileClick = onRepoFileClick,
        onDeleteFile = onRepoDeleteFile,
        selectedFilename = selectedRepoFilename,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onErrorDismiss = onErrorDismiss,
        onClearPickedFile = onRepoClearPickedFile,
        onPickFile = onRepoPickFile,
        onUploadFile = onRepoUploadFile
    )
}

@Composable
fun TaskDetailsScreenWithData(
    uiState: TaskDetailsUiState,
    onBack: () -> Unit,
    onOpenTimeAndAssignees: () -> Unit,
    onEditStatus: (TaskStatus) -> Unit,
    onEditTask: (String, String, TaskUrgency, TaskComplexity) -> Unit,
    onDownloadFile: (TaskFileUi) -> Unit,
    onDeleteFile: (TaskFileUi) -> Unit,
    onAddFile: () -> Unit
) {
    var showStatusDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    TaskDetailsScreen(
        taskTitle = uiState.taskTitle,
        status = uiState.status,
        priority = uiState.priority,
        volume = uiState.complexity,
        timeAndAssigneesTitle = "${uiState.totalHours}h total - ${uiState.trackingRecords.size} records",
        description = uiState.description,
        files = uiState.files,
        onBack = onBack,
        onOpenTimeAndAssignees = onOpenTimeAndAssignees,
        onEditStatus = { showStatusDialog = true },
        onEditTask = { showEditDialog = true },
        onDownloadFile = onDownloadFile,
        onDeleteFile = onDeleteFile,
        onAddFile = onAddFile
    )

    if (showStatusDialog) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = { Text("Select status", fontFamily = NunitoFamily) },
            text = {
                Column {
                    TaskStatus.entries.forEach { status ->
                        TextButton(
                            onClick = {
                                onEditStatus(status)
                                showStatusDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (status) {
                                                TaskStatus.TO_DO -> Color(0xFF6B7280)
                                                TaskStatus.IN_PROGRESS -> Color(0xFF3B82F6)
                                                TaskStatus.REVIEW -> Color(0xFF8B5CF6)
                                                TaskStatus.IN_TEST -> Color(0xFFF59E0B)
                                                TaskStatus.DONE -> Color(0xFF16A34A)
                                            }
                                        )
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = status.title,
                                    fontFamily = NunitoFamily,
                                    fontWeight = if (status == uiState.status) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text("Cancel", fontFamily = NunitoFamily)
                }
            }
        )
    }

    if (showEditDialog) {
        EditTaskDialog(
            initialTitle = uiState.taskTitle,
            initialDescription = uiState.description,
            initialPriority = uiState.priority,
            initialComplexity = uiState.complexity,
            onDismiss = { showEditDialog = false },
            onSave = { title, description, priority, complexity ->
                onEditTask(title, description, priority, complexity)
                showEditDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTaskDialog(
    initialTitle: String,
    initialDescription: String,
    initialPriority: TaskUrgency,
    initialComplexity: TaskComplexity,
    onDismiss: () -> Unit,
    onSave: (String, String, TaskUrgency, TaskComplexity) -> Unit
) {
    var title by remember(initialTitle) { mutableStateOf(initialTitle) }
    var description by remember(initialDescription) { mutableStateOf(initialDescription) }
    var priority by remember(initialPriority) { mutableStateOf(initialPriority) }
    var complexity by remember(initialComplexity) { mutableStateOf(initialComplexity) }
    var priorityOpen by remember { mutableStateOf(false) }
    var complexityOpen by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit task", fontFamily = NunitoFamily) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    label = { Text("Title", fontFamily = NunitoFamily) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", fontFamily = NunitoFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                ExposedDropdownMenuBox(
                    expanded = priorityOpen,
                    onExpandedChange = { priorityOpen = !priorityOpen }
                ) {
                    OutlinedTextField(
                        value = priority.title,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority", fontFamily = NunitoFamily) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityOpen) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = priorityOpen,
                        onDismissRequest = { priorityOpen = false }
                    ) {
                        TaskUrgency.entries.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt.title, fontFamily = NunitoFamily) },
                                onClick = {
                                    priority = opt
                                    priorityOpen = false
                                }
                            )
                        }
                    }
                }
                ExposedDropdownMenuBox(
                    expanded = complexityOpen,
                    onExpandedChange = { complexityOpen = !complexityOpen }
                ) {
                    OutlinedTextField(
                        value = complexity.title,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Complexity", fontFamily = NunitoFamily) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = complexityOpen) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = complexityOpen,
                        onDismissRequest = { complexityOpen = false }
                    ) {
                        TaskComplexity.entries.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt.title, fontFamily = NunitoFamily) },
                                onClick = {
                                    complexity = opt
                                    complexityOpen = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title, description, priority, complexity) },
                enabled = title.isNotBlank()
            ) {
                Text("Save", fontFamily = NunitoFamily)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontFamily = NunitoFamily)
            }
        }
    )
}

@Composable
fun TimeAndAssigneesScreenWithData(
    uiState: TaskDetailsUiState,
    onBack: () -> Unit,
    onAddTimeRecord: (String, AssigneeUi?) -> Unit,
    onDeleteEntry: (TimeEntryUi) -> Unit,
    onAssignResponsible: (AssigneeUi) -> Unit
) {
    val entries = uiState.trackingRecords.map { tracking ->
        TimeEntryUi(
            id = tracking.id.toString(),
            date = tracking.date.format(DateTimeFormatter.ISO_LOCAL_DATE),
            hoursText = "${tracking.hours} hours",
            userName = "${tracking.employeeFirstName ?: ""} ${tracking.employeeSecondName ?: ""}".trim()
        )
    }

    TimeAndAssigneesScreen(
        taskTitle = uiState.taskTitle,
        totalTimeText = "${uiState.totalHours} hours",
        createdBy = AssigneeUi("1", uiState.createdBy),
        responsible = AssigneeUi("2", uiState.assignedTo.ifEmpty { uiState.createdBy }),
        availableUsers = uiState.availableAssignees,
        timeHistoryTitle = "Time history (${entries.size} records)",
        entries = entries,
        onBack = onBack,
        onAddTimeRecord = onAddTimeRecord,
        onDeleteEntry = onDeleteEntry,
        onAssignResponsible = onAssignResponsible
    )
}

@Composable
fun InviteMemberScreenWithData(
    projectName: String,
    onBack: () -> Unit,
    onInvite: (String, String) -> Unit,
    isLoading: Boolean = false,
    error: String? = null
) {
    InviteMemberScreen(
        projectName = projectName,
        onBack = onBack,
        onInvite = onInvite,
        isLoading = isLoading,
        error = error
    )
}

@Composable
fun PersonalAccountScreenWithData(
    uiState: ProfileUiState,
    onProjectsClick: () -> Unit,
    onLogout: () -> Unit,
    selectedPhotoBytes: ByteArray?,
    onPickPhoto: () -> Unit,
    onSaveProfile: (String?, String?) -> Unit,
    onCancelEdit: () -> Unit
) {
    val profile = uiState.profile

    val myProjects = profile?.projects
        ?.map { project -> AccountProjectItem(project.id.toString(), project.name) }
        ?: emptyList()

    val myTasks = profile?.tasks
        ?.map { task ->
            AccountTaskItem(task.id.toString(), task.name, task.status)
        } ?: emptyList()

    val profileName = listOfNotNull(
        profile?.lastName?.takeIf { it.isNotBlank() },
        profile?.firstName?.takeIf { it.isNotBlank() },
        profile?.secondName?.takeIf { it.isNotBlank() }
    ).joinToString(" ").ifBlank { profile?.username ?: "" }

    PersonalAccountScreen(
        myProjects = myProjects,
        myTasks = myTasks,
        profileName = profileName,
        email = profile?.username ?: "",
        profileImageBase64 = profile?.profileImage,
        selectedPhotoBytes = selectedPhotoBytes,
        initialFirstName = profile?.firstName ?: "",
        initialLastName = profile?.lastName ?: "",
        initialSecondName = profile?.secondName ?: "",
        onProjectsClick = onProjectsClick,
        onLogout = onLogout,
        onPickPhoto = onPickPhoto,
        onSaveProfile = onSaveProfile,
        onCancelEdit = onCancelEdit
    )
}
