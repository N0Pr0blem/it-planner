package com.example.planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.planner.ui.components.AuthTabSwitcher
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.GreenButton

@Composable
fun RegisterScreen(onTabSwitch: (String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthTabSwitcher(selectedTab = "Register", onTabSelected = onTabSwitch)

            Spacer(Modifier.height(28.dp))

            // --- Карточка регистрации ---
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Task Planner",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.Black)
                    )

                    Spacer(Modifier.height(16.dp))

                    // --- Username ---
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Black) },
                        placeholder = { Text("Username", style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(0.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // --- Email ---
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Black) },
                        placeholder = { Text("Email", style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(0.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // --- Password ---
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Black) },
                        placeholder = { Text("Password", style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(0.dp)
                    )

                    Spacer(Modifier.height(48.dp)) // чуть больше воздуха, карточка длиннее
                }
            }

            // --- Кнопка Register ---
            Button(
                onClick = { /* TODO: register logic */ },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(GreenButton),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier
                    .offset(y = (-24).dp)      // легкое наложение на карточку
                    .fillMaxWidth(0.5f)
                    .height(46.dp)
            ) {
                Text(
                    "Register",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
