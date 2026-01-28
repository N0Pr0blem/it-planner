package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.theme.PlannerTheme

@Immutable
data class ProjectUi(
    val id: String,
    val name: String,
    val date: String, // "09.09.25"
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectsScreen(
    projects: List<ProjectUi>,
    selectedProjectId: String? = null,
    isLoading: Boolean = false,
    onAddProject: () -> Unit = {},
    onProjectClick: (ProjectUi) -> Unit = {},
    onProjectLongClick: (ProjectUi) -> Unit = {},
    onDeleteProject: (ProjectUi) -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    val navFg = Color(0xFF2D5178)
    val navBg = Color(0xFFE8E8E8)
    val cardBg = Color.White
    val selectedStroke = Color(0xFF1CCFC9)

    var deleteTarget by remember { mutableStateOf<ProjectUi?>(null) }

    if (deleteTarget != null) {
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            containerColor = Color(0xFFBDBDBD),
            shape = RoundedCornerShape(18.dp),
            text = {
                Text(
                    text = "Are you sure you want to\ndelete the project?",
                    color = Color.Black,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        deleteTarget?.let { onDeleteProject(it) }
                        deleteTarget = null
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Yes", color = Color.Black, fontFamily = NunitoFamily, fontWeight = FontWeight.Medium)
                }
            },
            dismissButton = {
                Button(
                    onClick = { deleteTarget = null },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Cancel", color = Color.White, fontFamily = NunitoFamily, fontWeight = FontWeight.Medium)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ---------- Top bar (+ справа) ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onAddProject,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add project",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // ---------- List ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 22.dp,
                        end = 22.dp,
                        top = 8.dp,
                        bottom = 18.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(projects, key = { it.id }) { p ->
                        ProjectCard(
                            project = p,
                            selected = p.id == selectedProjectId,
                            bg = cardBg,
                            stroke = selectedStroke,
                            onClick = { onProjectClick(p) },
                            onLongPress = { onProjectLongClick(p) }
                        )
                    }
                }

                if (isLoading && projects.isEmpty()) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // ---------- Bottom nav (как раньше) ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp))
                    .background(navBg),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.combinedClickable(
                            onClick = { /* Проекты уже открыты */ },
                            onLongClick = {}
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = "Projects",
                            tint = navFg,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            "Projects",
                            color = navFg,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.combinedClickable(
                            onClick = onAccountClick,
                            onLongClick = {}
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Account",
                            tint = navFg,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            "Account",
                            color = navFg,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProjectCard(
    project: ProjectUi,
    selected: Boolean,
    bg: Color,
    stroke: Color,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
) {
    val shape = RoundedCornerShape(22.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress
            ),
        color = bg,
        shape = shape,
        border = if (selected) BorderStroke(2.dp, stroke) else null,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Text(
                text = project.name,
                color = Color.Black,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.TopStart)
            )
            if (project.date.isNotEmpty()) {
                Text(
                    text = "Date:${project.date}",
                    color = Color.Black,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Preview(
    name = "Projects – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewProjects() {
    PlannerTheme {
        ProjectsScreen(
            projects = listOf(
                ProjectUi("1", "ProjectName", "09.09.25"),
                ProjectUi("2", "ProjectName", "09.09.25"),
                ProjectUi("3", "ProjectName", "09.09.25"),
                ProjectUi("4", "ProjectName", "09.09.25"),
            ),
            selectedProjectId = "2"
        )
    }
}

@Preview(
    name = "Projects – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewProjectsDark() {
    PlannerTheme {
        ProjectsScreen(
            projects = listOf(
                ProjectUi("1", "ProjectName", "09.09.25"),
                ProjectUi("2", "ProjectName", "09.09.25"),
                ProjectUi("3", "ProjectName", "09.09.25"),
            ),
            selectedProjectId = "2"
        )
    }
}
