package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@androidx.compose.runtime.Immutable
data class ProjectUserUi(
    val id: String,
    val fullName: String,
    val roleInProject: String,
    val firstName: String,
    val lastName: String,
)

@Composable
fun ProjectUserDetailsScreen(
    user: ProjectUserUi = ProjectUserUi(
        id = "5",
        fullName = "Olga Sliapitsa",
        roleInProject = "Backend Developer",
        firstName = "Olga",
        lastName = "Sliapitsa"
    ),
    onBack: () -> Unit = {},            // TODO: навигация назад
    onKickFromProject: () -> Unit = {}, // TODO: выгнать из проекта (подтверждение сделаете позже)
    onOpenMenuAction: (String) -> Unit = {}, // TODO: действия из меню (например "Edit", "Copy id" и т.п.)
) {
    var role by remember { mutableStateOf(user.roleInProject) }
    var isEditingRole by remember { mutableStateOf(false) }

    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // ---------- Top bar ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
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
                    text = user.fullName,
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("TODO: Edit", fontFamily = NunitoFamily) },
                            onClick = {
                                menuExpanded = false
                                onOpenMenuAction("edit")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("TODO: Copy ID", fontFamily = NunitoFamily) },
                            onClick = {
                                menuExpanded = false
                                onOpenMenuAction("copy_id")
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(22.dp))

            // ---------- Avatar + name ----------
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(104.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.fullName.firstOrNull()?.uppercase() ?: "",
                        color = Color.White,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 34.sp
                    )
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = user.fullName,
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "ID: ${user.id}",
                    color = Color.White.copy(alpha = 0.75f),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(18.dp))

            // ---------- Role in project (editable like in example) ----------
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text(
                        text = "Role in project",
                        color = Color.Black.copy(alpha = 0.45f),
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )

                    Spacer(Modifier.height(6.dp))

                    Box {
                        OutlinedTextField(
                            value = role,
                            onValueChange = { role = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            readOnly = !isEditingRole,
                            trailingIcon = {
                                IconButton(onClick = { isEditingRole = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit role",
                                        tint = Color(0xFF16A34A)
                                    )
                                }
                            },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = Color.Black.copy(alpha = 0.15f),
                                unfocusedBorderColor = Color.Black.copy(alpha = 0.12f),
                                cursorColor = Color.Black
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )

                        // тап по всему полю -> включить редактирование (как в твоем PersonalAccount)
                        if (!isEditingRole) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { isEditingRole = true }
                            )
                        }
                    }

                    // маленькая подсказка
                    if (isEditingRole) {
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    // TODO: сохранить роль на бэк
                                    isEditingRole = false
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Text(
                                    text = "Save",
                                    color = Color.White,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Button(
                                onClick = {
                                    role = user.roleInProject
                                    isEditingRole = false
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = Color.White,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------- Fields: first/last name (read only, like example) ----------
            InfoField(label = "First name", value = user.firstName)
            Spacer(Modifier.height(12.dp))
            InfoField(label = "Last name", value = user.lastName)

            Spacer(Modifier.weight(1f))

            // ---------- Kick button ----------
            Button(
                onClick = onKickFromProject,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(bottom = 18.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444))
            ) {
                Text(
                    text = "Kick from project",
                    color = Color(0xFFEF4444),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun InfoField(
    label: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                text = label,
                color = Color.Black.copy(alpha = 0.45f),
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                color = Color.Black,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(
    name = "Project User Details – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewProjectUserDetails() {
    PlannerTheme {
        ProjectUserDetailsScreen()
    }
}

@Preview(
    name = "Project User Details – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewProjectUserDetailsDark() {
    PlannerTheme { PreviewProjectUserDetails() }
}
