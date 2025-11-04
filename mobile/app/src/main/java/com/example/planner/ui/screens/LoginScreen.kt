package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.planner.ui.components.AuthTabSwitcher
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.GreenButton
import com.example.planner.ui.theme.PlannerTheme
import androidx.compose.ui.text.style.TextAlign
import com.example.planner.ui.theme.NunitoFamily

@Preview(
    name = "Login – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewLoginScreen() {
    PlannerTheme {

        Box(Modifier.fillMaxSize().background(BlueBackground)) {
            LoginScreen (onTabSwitch = {})
        }
    }
}

@Preview(
    name = "Login – System UI",
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewLoginSystemUi() {
    PlannerTheme { LoginScreen (onTabSwitch = {}) }
}

@Preview(
    name = "Login – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewLoginDark() {
    PlannerTheme { LoginScreen (onTabSwitch = {}) }
}

@Composable
fun LoginScreen(onTabSwitch: (String) -> Unit) {
    var username by remember { mutableStateOf("") }
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
            AuthTabSwitcher(selectedTab = "Login", onTabSelected = onTabSwitch)

            Spacer(Modifier.height(36.dp))

            // Карточка компактная, по контенту
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Task Planner",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontFamily = NunitoFamily
                    )

                    Spacer(Modifier.height(40.dp))

                    // Подчёркнутые поля — используем TextField c индикатором
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
                            disabledContainerColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(0.dp) // без скругления, тонкая линия
                    )

                    Spacer(Modifier.height(36.dp))

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
                            disabledContainerColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(0.dp)
                    )

                    Spacer(Modifier.height(60.dp))
                }
            }

            // Кнопка ПОД карточкой, визуально «приклеена» к ней
            Button(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(GreenButton),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier
                    .offset(y = (-24).dp)
                    .fillMaxWidth(0.45f)
                    .height(46.dp)
            ) {
                Text(
                    "Login",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White, fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}
