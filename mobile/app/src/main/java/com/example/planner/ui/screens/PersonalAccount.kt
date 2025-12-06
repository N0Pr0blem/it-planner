package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.theme.PlannerTheme

private enum class EditField { Login, Password }

@Composable
fun PersonalAccountScreen() {

    // "сохранённые" значения (пока заглушки)
    var savedLogin by remember { mutableStateOf("PersonLogin") }
    var savedPassword by remember { mutableStateOf("password123") }

    // значения в полях (редактируемые)
    var login by remember { mutableStateOf(savedLogin) }
    var password by remember { mutableStateOf(savedPassword) }

    // режим редактирования + какое поле нужно сфокусировать
    var isEditing by remember { mutableStateOf(false) }
    var focusField by remember { mutableStateOf<EditField?>(null) }

    val loginFocus = remember { FocusRequester() }
    val passFocus = remember { FocusRequester() }

    // когда включили редактирование — ставим фокус в нужное поле
    LaunchedEffect(isEditing, focusField) {
        if (isEditing) {
            when (focusField) {
                EditField.Login -> loginFocus.requestFocus()
                EditField.Password -> passFocus.requestFocus()
                null -> Unit
            }
        }
    }

    fun startEditing(field: EditField) {
        login = savedLogin
        password = savedPassword
        isEditing = true
        focusField = field
    }

    fun cancelEditing() {
        login = savedLogin
        password = savedPassword
        isEditing = false
        focusField = null
    }

    fun saveEditing() {
        savedLogin = login
        savedPassword = password
        isEditing = false
        focusField = null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
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
                IconButton(onClick = { /* TODO: Logout */ }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(46.dp))

            // -------- Form --------
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f), // чтобы нижний блок всегда был снизу
                horizontalAlignment = Alignment.Start
            ) {
                // -------- Login --------
                Text(
                    "Login",
                    color = Color.White,
                    modifier = Modifier.padding(start = 14.dp),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))

                Box {
                    OutlinedTextField(
                        value = login,
                        onValueChange = { login = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(loginFocus)
                            .padding(top = 4.dp),
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = Color.White) },
                        singleLine = true,
                        readOnly = !isEditing,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                            cursorColor = Color.White,
                            disabledTextColor = Color.White
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontFamily = NunitoFamily,
                            fontSize = 16.sp
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )

                    // Прозрачный слой для тапа — только когда НЕ редактируем
                    if (!isEditing) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { startEditing(EditField.Login) }
                        )
                    }
                }

                Spacer(Modifier.height(26.dp))

                // -------- Password --------
                Text(
                    "Password",
                    color = Color.White,
                    modifier = Modifier.padding(start = 14.dp),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))

                Box {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.White) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passFocus)
                            .padding(top = 4.dp),
                        readOnly = !isEditing,
                        visualTransformation = if (isEditing) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                            cursorColor = Color.White,
                            disabledTextColor = Color.White
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontFamily = NunitoFamily,
                            fontSize = 16.sp
                        ),
                        shape = RoundedCornerShape(15.dp)
                    )

                    if (!isEditing) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { startEditing(EditField.Password) }
                        )
                    }
                }

                // -------- Save / Cancel (только когда редактируем) --------
                if (isEditing) {
                    Spacer(Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Button(
                            onClick = { saveEditing() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                        ) {
                            Text(
                                "Save",
                                color = Color.Black,
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Button(
                            onClick = { cancelEditing() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                        ) {
                            Text(
                                "Cancel",
                                color = Color.White,
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // -------- Bottom menu (как раньше) --------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp))
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { /* TODO: go to Projects */ }
                    ) {
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

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { /* TODO: go to Settings (Account) */ }
                    ) {
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
