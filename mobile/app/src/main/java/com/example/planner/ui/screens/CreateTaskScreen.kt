package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
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
import com.example.planner.ui.theme.GreenButton
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.theme.PlannerTheme

enum class TaskPriority(val label: String) {
    URGENT("Urgent"),
    NORMAL("Normal"),
    LOW("Low")
}

enum class TaskVolume(val label: String) {
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onBack: () -> Unit = {},
    onCreate: (title: String, description: String, priority: TaskPriority, volume: TaskVolume) -> Unit = { _, _, _, _ -> }
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var priority by remember { mutableStateOf(TaskPriority.NORMAL) }
    var volume by remember { mutableStateOf(TaskVolume.MEDIUM) }

    var priorityOpen by remember { mutableStateOf(false) }
    var volumeOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 18.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
        ) {
            // ----- Top bar -----
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
                    text = "Create task",
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Spacer(Modifier.size(44.dp)) // симметрия
            }

            Spacer(Modifier.height(18.dp))

            // ----- Карточка -----
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp)
                ) {
                    // --- Title ---
                    Text(
                        text = "Title",
                        color = Color.Black,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                        singleLine = true,
                        placeholder = { Text("Task name...", fontFamily = NunitoFamily) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // --- Priority + Volume (2 columns) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Priority
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Priority",
                                color = Color.Black,
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
                            )

                            ExposedDropdownMenuBox(
                                expanded = priorityOpen,
                                onExpandedChange = { priorityOpen = !priorityOpen }
                            ) {
                                OutlinedTextField(
                                    value = priority.label,
                                    onValueChange = {},
                                    readOnly = true,
                                    singleLine = true,
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityOpen) },
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                                )

                                ExposedDropdownMenu(
                                    expanded = priorityOpen,
                                    onDismissRequest = { priorityOpen = false }
                                ) {
                                    TaskPriority.entries.forEach { opt ->
                                        DropdownMenuItem(
                                            text = { Text(opt.label, fontFamily = NunitoFamily) },
                                            onClick = {
                                                priority = opt
                                                priorityOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Volume",
                                color = Color.Black,
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
                            )

                            ExposedDropdownMenuBox(
                                expanded = volumeOpen,
                                onExpandedChange = { volumeOpen = !volumeOpen }
                            ) {
                                OutlinedTextField(
                                    value = volume.label,
                                    onValueChange = {},
                                    readOnly = true,
                                    singleLine = true,
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = volumeOpen) },
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                                )

                                ExposedDropdownMenu(
                                    expanded = volumeOpen,
                                    onDismissRequest = { volumeOpen = false }
                                ) {
                                    TaskVolume.entries.forEach { opt ->
                                        DropdownMenuItem(
                                            text = { Text(opt.label, fontFamily = NunitoFamily) },
                                            onClick = {
                                                volume = opt
                                                volumeOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // --- Описание ---
                    Text(
                        text = "Description",
                        color = Color.Black,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Enter task description...", fontFamily = NunitoFamily) },
                        minLines = 6,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 160.dp),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ----- Создание кнопки -----
            Button(
                onClick = { /* TODO */ onCreate(title, description, priority, volume) },
                enabled = title.isNotBlank(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenButton),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Create",
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(
    name = "Create task – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewCreateTask() {
    PlannerTheme { CreateTaskScreen() }
}

@Preview(
    name = "Create task – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewCreateTaskDark() {
    PlannerTheme { CreateTaskScreen() }
}
