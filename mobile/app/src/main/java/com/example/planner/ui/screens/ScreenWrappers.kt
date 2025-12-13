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
import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.data.model.task.TaskUrgency
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.viewmodel.*
import com.example.planner.data.mapper.toUi
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
    onTabChange: (ProjectTab) -> Unit
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
        onTaskClick = onTaskClick
    )
}

@Composable
fun TaskDetailsScreenWithData(
    uiState: TaskDetailsUiState,
    onBack: () -> Unit,
    onOpenTimeAndAssignees: () -> Unit,
    onEditStatus: (TaskStatus) -> Unit
) {
    TaskDetailsScreen(
        taskTitle = uiState.taskTitle,
        status = uiState.status,
        priority = uiState.priority,
        volume = uiState.complexity,
        timeAndAssigneesTitle = "${uiState.totalHours}h total • ${uiState.trackingRecords.size} records",
        description = uiState.description,
        files = uiState.files,
        onBack = onBack,
        onOpenTimeAndAssignees = onOpenTimeAndAssignees,
        onEditStatus = { onEditStatus(uiState.status) }
    )
}

@Composable
fun TimeAndAssigneesScreenWithData(
    uiState: TaskDetailsUiState,
    onBack: () -> Unit,
    onAddTimeRecord: (String, AssigneeUi?) -> Unit,
    onDeleteEntry: (TimeEntryUi) -> Unit
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
        timeHistoryTitle = "Time history (${entries.size} records)",
        entries = entries,
        onBack = onBack,
        onAddTimeRecord = onAddTimeRecord,
        onDeleteEntry = onDeleteEntry
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
        onInvite = onInvite
    )
}

@Composable
fun PersonalAccountScreenWithData(
    uiState: ProfileUiState,
    onProjectsClick: () -> Unit,
    onLogout: () -> Unit,
    onSaveProfile: (String, String) -> Unit
) {
    val profile = uiState.profile
    
    // Преобразуем данные для экранного компонента
    val myProjects = profile?.projects?.map { project ->
        AccountProjectItem(project.id.toString(), project.name)
    } ?: emptyList()
    
    val myTasks = profile?.tasks?.map { task ->
        AccountTaskItem(task.id.toString(), task.name, if (task.isCompleted) TaskStatus.DONE else TaskStatus.TO_DO)
    } ?: emptyList()
    
    PersonalAccountScreen(
        myProjects = myProjects,
        myTasks = myTasks,
        savedLogin = profile?.username ?: "",
        savedPassword = "", // Пароль не хранится в профиле по соображениям безопасности
        onProjectsClick = onProjectsClick,
        onLogout = onLogout,
        onSaveProfile = onSaveProfile
    )
}
