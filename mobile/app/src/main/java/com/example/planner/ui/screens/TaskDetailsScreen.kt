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
import com.example.planner.domain.model.TaskStatus
import com.example.planner.domain.model.TaskUrgency
import com.example.planner.domain.model.TaskComplexity
import com.example.planner.ui.extensions.dotColor
import com.example.planner.ui.extensions.title


fun TaskUrgency.color(): Color = when (this) {
    TaskUrgency.URGENT -> Color(0xFFEF4444)
    TaskUrgency.MEDIUM -> Color(0xFFF59E0B)
    TaskUrgency.NOT_URGENT -> Color(0xFF16A34A)
}

fun TaskComplexity.color(): Color = when (this) {
    TaskComplexity.HARD -> Color(0xFF8B5CF6)
    TaskComplexity.MEDIUM -> Color(0xFF60A5FA)
    TaskComplexity.EASY -> Color(0xFF16A34A)
}

data class TaskFileUi(
    val id: String,
    val fileName: String?,
)

// ======= screen =======

@Composable
fun TaskDetailsScreen(
    taskTitle: String = "Taska",
    status: TaskStatus = TaskStatus.REVIEW,
    priority: TaskUrgency = TaskUrgency.MEDIUM,
    volume: TaskComplexity = TaskComplexity.HARD,
    timeAndAssigneesTitle: String = "10.0h total • 2 records", // заглушка
    description: String = "chill",
    files: List<TaskFileUi> = listOf(TaskFileUi("1", "CV.pdf")),

    onBack: () -> Unit = {},
    onEditTask: () -> Unit = {},                // иконка карандаша в шапке
    onEditStatus: () -> Unit = {},              // карандаш в карточке статуса
    onOpenTimeAndAssignees: () -> Unit = {},    // карточка "Time & assignees"
    onRefreshDescription: () -> Unit = {},      // иконка "обновить" у описания
    onDownloadFile: (TaskFileUi) -> Unit = {},  // скачать
    onDeleteFile: (TaskFileUi) -> Unit = {},    // удалить
    onAddFile: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 18.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                // ===== Top bar =====
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /* TODO */ onBack() },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    Text(
                        text = taskTitle,
                        color = Color.White,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                    IconButton(
                        onClick = { /* TODO */ onEditTask() },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit task", tint = Color.White)
                    }
                }
            }

            item {
                Spacer(Modifier.height(2.dp))
            }

            item {
                //  Status card
                WhiteCard {
                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            text = "Status",
                            color = Color.Black.copy(alpha = 0.45f),
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // точка статуса
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(status.dotColor())
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = status.title,
                                    color = Color.Black,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            }

                            IconButton(
                                onClick = { /* TODO */ onEditStatus() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit status",
                                    tint = Color(0xFF16A34A)
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Priority + Volume row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WhiteCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Text(
                                text = "Priority",
                                color = Color.Black.copy(alpha = 0.45f),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = priority.title,
                                color = priority.color(),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    WhiteCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Text(
                                text = "Volume",
                                color = Color.Black.copy(alpha = 0.45f),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = volume.title,
                                color = volume.color(),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            item {
                // Time & assignees
                WhiteCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO */ onOpenTimeAndAssignees() }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE7F4EA)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = Color(0xFF16A34A)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Time & assignees",
                                color = Color.Black,
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = timeAndAssigneesTitle,
                                color = Color.Black.copy(alpha = 0.55f),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Tap for details",
                                color = Color.Black.copy(alpha = 0.35f),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.Black.copy(alpha = 0.35f)
                        )
                    }
                }
            }

            item {
                // Description header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Description",
                        color = Color.White,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                    IconButton(onClick = { /* TODO */ onRefreshDescription() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                    }
                }
            }

            item {
                //  Description box
                Surface(
                    color = Color(0xFFE1E1E1),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (description.isBlank()) "No description" else description,
                        color = Color.Black.copy(alpha = 0.85f),
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            item {
                //  Attachments title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Attached files",
                        color = Color.White,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    TextButton(onClick = onAddFile) {
                        Text(
                            text = "Add file",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            items(files, key = { it.id }) { f ->
                WhiteCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = f.fileName ?: "",
                            color = Color.Black,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(onClick = { /* TODO */ onDownloadFile(f) }) {
                            Icon(Icons.Default.Download, contentDescription = "Download", tint = Color(0xFF16A34A))
                        }
                        IconButton(onClick = { /* TODO */ onDeleteFile(f) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                        }
                    }
                }
            }

            item {
                // немного воздуха в конце (если захотите кнопку снизу позже)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun WhiteCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            content = content
        )
    }
}

// previews

@Preview(
    name = "Task details – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewTaskDetails() {
    PlannerTheme {
        TaskDetailsScreen(
            taskTitle = "Taska",
            status = TaskStatus.REVIEW,
            priority = TaskUrgency.MEDIUM,
            volume = TaskComplexity.HARD,
            timeAndAssigneesTitle = "10.0h total • 2 records",
            description = "chill",
            files = listOf(TaskFileUi("1", "CV.pdf"))
        )
    }
}

@Preview(
    name = "Task details – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewTaskDetailsDark() {
    PlannerTheme { PreviewTaskDetails() }
}





