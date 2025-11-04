package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.GreenButton
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.theme.PlannerTheme

@Composable
fun PersonalAccountScreen() {
    var login by remember { mutableStateOf("PersonLogin") }
    var password by remember { mutableStateOf("password123") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
        ) {
            // -------- Header --------
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            "Personal account",
                            color = Color.White,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        Text(
                            "Email@gmail.com",
                            color = Color.White.copy(alpha = 0.8f),
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
                IconButton(onClick = { /* Logout */ }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(46.dp))

            Column(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Login",
                    color = Color.White,
                    modifier = Modifier.padding(start = 14.dp),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),

                    leadingIcon = {
                        Icon(Icons.Default.Person, null, tint = Color.White)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                        cursorColor = Color.White
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = NunitoFamily,
                        fontSize = 16.sp
                    ),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(26.dp))

                Text(
                    "Password",
                    color = Color.White,
                    modifier = Modifier.padding(start = 14.dp),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                        cursorColor = Color.White
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = NunitoFamily,
                        fontSize = 16.sp
                    ),
                    shape = RoundedCornerShape(15.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 80.dp,
                            topEnd = 80.dp
                        )
                    )
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = "Projects",
                            tint = Color(0xFF2D5178),
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            "Projects",
                            color = Color(0xFF2D5178),
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Setting",
                            tint = Color(0xFF2D5178),
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            "Setting",
                            color = Color(0xFF2D5178),
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

@Preview(
    name = "Personal Account – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewPersonalAccount() {
    PlannerTheme { PersonalAccountScreen() }
}

@Preview(
    name = "Personal Account – System UI",
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewPersonalAccountSystemUi() {
    PlannerTheme { PersonalAccountScreen() }
}

@Preview(
    name = "Personal Account – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewPersonalAccountDark() {
    PlannerTheme { PersonalAccountScreen() }
}
