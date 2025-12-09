package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.GreenButton
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.theme.PlannerTheme

// UI models (заглушки)

@Immutable
data class AssigneeUi(
    val id: String,
    val name: String,
)

@Immutable
data class TimeEntryUi(
    val id: String,
    val date: String,      // "2025-11-29"
    val hoursText: String, // "2.0 hours"
    val userName: String,  // "Olga Sliapitsa"
)

// ===== Screen =====

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeAndAssigneesScreen(
    taskTitle: String = "PLA-4",
    totalTimeText: String = "2.0 hours",

    createdBy: AssigneeUi = AssigneeUi("1", "Olga Sliapitsa"),
    responsible: AssigneeUi = AssigneeUi("2", "Olga Sliapitsa"),

    // список пользователей для bottom-sheet (пока заглушка)
    availableUsers: List<AssigneeUi> = listOf(
        AssigneeUi("1", "Olga Sliapitsa"),
        AssigneeUi("2", "G. K."),
        AssigneeUi("3", "Ivan Ivanov"),
    ),

    timeHistoryTitle: String = "Time history (1 record)",
    entries: List<TimeEntryUi> = listOf(
        TimeEntryUi("1", "2025-11-29", "2.0 hours", "Olga Sliapitsa")
    ),

    onBack: () -> Unit = {},
    onRefresh: () -> Unit = {},            // верхняя иконка refresh
    onRefreshHistory: () -> Unit = {},     // refresh у истории
    onDeleteEntry: (TimeEntryUi) -> Unit = {},

    // TODO: сюда потом подключишь реальное добавление записи (бек/вм)
    onAddTimeRecord: (hoursText: String, user: AssigneeUi?) -> Unit = { _, _ -> },

    onConfirmChanges: () -> Unit = {},
) {
    // ===== Bottom sheet state =====
    var showAddSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 18.dp)
        ) {
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
                    text = "Time & assignees",
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { /* TODO */ onRefresh() },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }

                    Spacer(Modifier.width(10.dp))

                    IconButton(
                        onClick = {
                            // открываем bottom-sheet
                            showAddSheet = true
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // ===== Content list (scroll) =====
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                // важно: чтобы список не уезжал под нижнюю кнопку Confirm
                contentPadding = PaddingValues(bottom = 92.dp)
            ) {
                item {
                    // ===== Total time green card =====
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenButton),
                        elevation = CardDefaults.cardElevation(0.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 18.dp)
                        ) {
                            Text(
                                text = "Total time spent",
                                color = Color.White.copy(alpha = 0.9f),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = totalTimeText,
                                    color = Color.White,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 28.sp
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = taskTitle,
                                color = Color.White.copy(alpha = 0.9f),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                item { SectionTitle(text = "Assignees") }

                item {
                    AssigneeCard(
                        icon = Icons.Default.Edit,
                        iconBg = Color(0xFFEAF2FF),
                        iconTint = Color(0xFF2563EB),
                        label = "Created task",
                        user = createdBy,
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    AssigneeCard(
                        icon = Icons.Default.Person,
                        iconBg = Color(0xFFE7F4EA),
                        iconTint = Color(0xFF16A34A),
                        label = "Responsible",
                        user = responsible,
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionTitle(text = timeHistoryTitle)

                        IconButton(onClick = { /* TODO */ onRefreshHistory() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh history", tint = Color.White)
                        }
                    }
                }

                items(entries, key = { it.id }) { e ->
                    TimeEntryCard(
                        entry = e,
                        onDelete = { /* TODO */ onDeleteEntry(e) }
                    )
                }
            }
        }

        // ===== Bottom confirm button (fixed) =====
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Button(
                onClick = { /* TODO */ onConfirmChanges() },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenButton),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "Confirm changes",
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }

        // ===== Add time record bottom-sheet =====
        if (showAddSheet) {
            AddTimeRecordBottomSheet(
                taskTitle = taskTitle,
                users = availableUsers,
                onDismiss = { showAddSheet = false },
                onCreate = { hoursText, user ->
                    // TODO: потом подключишь реальное сохранение/обновление
                    onAddTimeRecord(hoursText, user)
                    showAddSheet = false
                }
            )
        }
    }
}

// ===== Bottom sheet (верстка) =====

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTimeRecordBottomSheet(
    taskTitle: String,
    users: List<AssigneeUi>,
    onDismiss: () -> Unit,
    onCreate: (hoursText: String, user: AssigneeUi?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var hoursText by remember { mutableStateOf("") }
    var selectedUser by remember { mutableStateOf<AssigneeUi?>(users.firstOrNull()) }
    var expanded by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFF2F2F2),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 6.dp)
                    .size(width = 54.dp, height = 6.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.Black.copy(alpha = 0.12f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add time record",
                    color = Color.Black,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = taskTitle,
                color = Color.Black.copy(alpha = 0.6f),
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Assignee",
                color = Color.Black,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 6.dp, bottom = 6.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedUser?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black.copy(alpha = 0.25f),
                        unfocusedBorderColor = Color.Black.copy(alpha = 0.18f)
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    users.forEach { u ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    u.name,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            onClick = {
                                selectedUser = u
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Hours",
                color = Color.Black,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 6.dp, bottom = 6.dp)
            )

            OutlinedTextField(
                value = hoursText,
                onValueChange = { v ->
                    // лёгкая фильтрация: цифры + точка
                    hoursText = v.filter { it.isDigit() || it == '.' }
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null) },
                placeholder = {
                    Text("e.g. 2.0", fontFamily = NunitoFamily)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = VisualTransformation.None,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black.copy(alpha = 0.25f),
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.18f)
                )
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Date will be set automatically.",
                color = Color.Black.copy(alpha = 0.45f),
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.Black,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = { onCreate(hoursText, selectedUser) },
                    enabled = hoursText.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenButton),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Add",
                        color = Color.White,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(18.dp))
        }
    }
}

// ===== small components =====

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )
}

@Composable
private fun AssigneeCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    user: AssigneeUi,
    onClick: () -> Unit,
) {
    WhiteCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    color = Color.Black.copy(alpha = 0.45f),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = user.name,
                    color = Color.Black,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            AvatarCircle(name = user.name)
        }
    }
}

@Composable
private fun TimeEntryCard(
    entry: TimeEntryUi,
    onDelete: () -> Unit,
) {
    WhiteCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE7F4EA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFF16A34A))
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.date,
                    color = Color.Black,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${entry.hoursText} • ${entry.userName}",
                    color = Color.Black.copy(alpha = 0.6f),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
            }
        }
    }
}

@Composable
private fun AvatarCircle(name: String) {
    val initial = name.trim().firstOrNull()?.uppercase() ?: "?"
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFFCBD5F5)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            color = Color(0xFF1F2937),
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun WhiteCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            content = content
        )
    }
}

// ===== Preview =====

@Preview(
    name = "Time & assignees – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewTimeAndAssignees() {
    PlannerTheme {
        TimeAndAssigneesScreen()
    }
}

@Preview(
    name = "Time & assignees – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewTimeAndAssigneesDark() {
    PlannerTheme { PreviewTimeAndAssignees() }
}
