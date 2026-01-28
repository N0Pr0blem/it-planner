package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
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

@Composable
fun InviteMemberScreen(
    projectName: String,
    onBack: () -> Unit = {},
    onInvite: (email: String, role: String) -> Unit = { _, _ -> },
    isLoading: Boolean = false,
    error: String? = null
) {
    val roles = remember {
        listOf(
            "Backend Developer",
            "Frontend Developer",
            "Designer",
            "QA Engineer",
            "Project Manager"
        )
    }

    var email by remember { mutableStateOf("") }
    var roleExpanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(roles.first()) }

    val canInvite = email.trim().isNotEmpty() && !isLoading

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
                    text = projectName,
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Spacer(Modifier.size(44.dp))
            }

            Spacer(Modifier.height(34.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.92f) // уже карточка
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "Invite member",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = Color(0xFF0E3A22)
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "Email",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.75f)
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = Color(0xFF2F7D2F)
                                )
                            },
                            placeholder = { Text("user@example.com", fontFamily = NunitoFamily) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Black.copy(alpha = 0.15f),
                                unfocusedBorderColor = Color.Black.copy(alpha = 0.10f),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = Color.Black
                            )
                        )

                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Use the registered email.",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.55f)
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Project role",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.75f)
                        )
                        Spacer(Modifier.height(8.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White,
                                tonalElevation = 0.dp,
                                shadowElevation = 0.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { roleExpanded = true }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedRole,
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color.Black.copy(alpha = 0.45f)
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = roleExpanded,
                                onDismissRequest = { roleExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                roles.forEach { r ->
                                    DropdownMenuItem(
                                        text = { Text(r, fontFamily = NunitoFamily) },
                                        onClick = {
                                            selectedRole = r
                                            roleExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        if (!error.isNullOrBlank()) {
                            Text(
                                text = error,
                                color = Color(0xFFB91C1C),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = onBack) {
                                Text(
                                    text = "Back",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0E3A22)
                                )
                            }

                            Button(
                                onClick = { onInvite(email.trim(), selectedRole) },
                                enabled = canInvite,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenButton),
                                elevation = ButtonDefaults.buttonElevation(0.dp),
                                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 12.dp)
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(16.dp),
                                        color = Color.White
                                    )
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text(
                                    text = if (isLoading) "Inviting..." else "Invite",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Invite Member Screen - Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewInviteMemberScreen() {
    PlannerTheme {
        InviteMemberScreen(projectName = "Planner Mobile")
    }
}

@Preview(
    name = "Invite Member Screen - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewInviteMemberScreenDark() {
    PlannerTheme { PreviewInviteMemberScreen() }
}


