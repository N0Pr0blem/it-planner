package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.GreenButton
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.theme.PlannerTheme

// --- модели ---

enum class ProjectTab { Tasks, Members, Repository }

enum class TaskStatus(val label: String, val color: Color) {
    TODO("To do", Color(0xFF6B7280)),          // серый
    IN_REVIEW("In review", Color(0xFF8B5CF6)), // фиолетовый
    DONE("Done", Color(0xFF16A34A))            // зелёный
}

data class ProjectTaskUi(
    val id: String,
    val title: String,
    val assignee: String,
    val status: TaskStatus
)

// --- экран деталей проекта (верстка) ---

@Composable
fun ProjectDetailsScreen(
    projectName: String,
    tasks: List<ProjectTaskUi>,
    activeTab: ProjectTab = ProjectTab.Tasks,
    onTabChange: (ProjectTab) -> Unit = {},
    onBack: () -> Unit = {},
    onAddMember: () -> Unit = {},
    onAddTask: () -> Unit = {},
    onTaskClick: (ProjectTaskUi) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ✅ Контентная часть (с отступами)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                ProjectTopBar(
                    projectName = projectName,
                    onBack = { /* TODO */ onBack() },
                    onAddMember = { /* TODO */ onAddMember() }
                )

                Spacer(Modifier.height(18.dp))

                when (activeTab) {
                    ProjectTab.Tasks -> {
                        TasksHeader(onAddTask = { /* TODO */ onAddTask() })
                        Spacer(Modifier.height(14.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 12.dp)
                        ) {
                            items(tasks, key = { it.id }) { task ->
                                TaskCard(
                                    task = task,
                                    onClick = { /* TODO */ onTaskClick(task) }
                                )
                            }
                        }
                    }

                    ProjectTab.Members -> {
                        SectionTitle(title = "Project members")
                        Spacer(Modifier.height(14.dp))
                        MembersStub(modifier = Modifier.weight(1f))
                    }

                    ProjectTab.Repository -> {
                        SectionTitle(title = "Repository")
                        Spacer(Modifier.height(14.dp))
                        RepositoryStub(modifier = Modifier.weight(1f))
                    }
                }
            }

            // ✅ Нижний бар — БЕЗ горизонтальных паддингов, во всю ширину
            ProjectBottomBar(
                modifier = Modifier.fillMaxWidth(),
                activeTab = activeTab,
                onTabChange = onTabChange
            )
        }
    }
}

// --- top bar ---

@Composable
private fun ProjectTopBar(
    projectName: String,
    onBack: () -> Unit,
    onAddMember: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Text(
            text = projectName,
            color = Color.White,
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        IconButton(
            onClick = onAddMember,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f))
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = "Add member",
                tint = Color.White
            )
        }
    }
}

// --- tasks header ---

@Composable
private fun TasksHeader(
    onAddTask: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Project tasks",
            color = Color.White,
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        Button(
            onClick = onAddTask,
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenButton),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add task",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Add",
                color = Color.White,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )
}

// --- карточка задачи ---

@Composable
private fun TaskCard(
    task: ProjectTaskUi,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 86.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    color = Color.Black,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(task.status.color.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(task.status.color)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFCBD5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = task.assignee.firstOrNull()?.uppercase() ?: "",
                        color = Color(0xFF1F2937),
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = task.assignee,
                    color = Color(0xFF1F2937),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = task.status.label,
                color = task.status.color,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        }
    }
}

// --- нижнее меню (3 вкладки) ---

@Composable
private fun ProjectBottomBar(
    modifier: Modifier = Modifier,
    activeTab: ProjectTab,
    onTabChange: (ProjectTab) -> Unit
) {
    val navBg = Color(0xFFE8E8E8)
    val navFg = Color(0xFF2D5178)

    Box(
        modifier = modifier
            .height(160.dp)
            .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp))
            .background(navBg),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectBottomItem(
                icon = Icons.Default.List,
                label = "Tasks",
                tint = navFg,
                active = activeTab == ProjectTab.Tasks,
                onClick = { onTabChange(ProjectTab.Tasks) }
            )
            ProjectBottomItem(
                icon = Icons.Default.Groups,
                label = "Members",
                tint = navFg,
                active = activeTab == ProjectTab.Members,
                onClick = { onTabChange(ProjectTab.Members) }
            )
            ProjectBottomItem(
                icon = Icons.Default.Folder,
                label = "Repository",
                tint = navFg,
                active = activeTab == ProjectTab.Repository,
                onClick = { onTabChange(ProjectTab.Repository) }
            )
        }
    }
}

@Composable
private fun ProjectBottomItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    active: Boolean,
    onClick: () -> Unit
) {
    val alpha = if (active) 1f else 0.7f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint.copy(alpha = alpha),
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            color = tint.copy(alpha = alpha),
            fontFamily = NunitoFamily,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

// --- простые заглушки для вкладок ---

@Composable
private fun MembersStub(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "*(members screen later)*",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun RepositoryStub(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "*(repository screen later)*",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
    }
}

// --- превью ---

@Preview(
    name = "Project details – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewProjectDetails() {
    PlannerTheme {
        ProjectDetailsScreen(
            projectName = "Planner Mobile",
            tasks = listOf(
                ProjectTaskUi("1", "Taska", "Vlad Shugar", TaskStatus.TODO),
                ProjectTaskUi("2", "Taska", "Valera Pompish", TaskStatus.IN_REVIEW),
                ProjectTaskUi("3", "Taska", "Kostia Tigrovech", TaskStatus.IN_REVIEW),
                ProjectTaskUi("4", "Taska", "Pasha Paulski", TaskStatus.DONE),
            ),
            activeTab = ProjectTab.Tasks,
            onTabChange = {}
        )
    }
}

@Preview(
    name = "Project details – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewProjectDetailsDark() {
    PlannerTheme {
        PreviewProjectDetails()
    }
}
