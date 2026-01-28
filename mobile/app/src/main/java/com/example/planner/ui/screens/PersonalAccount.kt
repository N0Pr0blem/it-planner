package com.example.planner.ui.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.theme.PlannerTheme
import com.example.planner.domain.model.TaskStatus
import com.example.planner.ui.extensions.dotColor
import com.example.planner.ui.extensions.title

data class AccountProjectItem(val id: String, val name: String)

data class AccountTaskItem(val id: String, val title: String, val status: TaskStatus)

@Composable
fun PersonalAccountScreen(
    myProjects: List<AccountProjectItem> = emptyList(),
    myTasks: List<AccountTaskItem> = emptyList(),
    profileName: String = "",
    email: String = "",
    profileImageBase64: String? = null,
    selectedPhotoBytes: ByteArray? = null,
    initialFirstName: String = "",
    initialLastName: String = "",
    initialSecondName: String = "",
    onProjectsClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onPickPhoto: () -> Unit = {},
    onSaveProfile: (String?, String?) -> Unit = { _, _ -> },
    onCancelEdit: () -> Unit = {}
) {
    var lastName by remember(initialLastName) { mutableStateOf(initialLastName) }
    var secondName by remember(initialSecondName) { mutableStateOf(initialSecondName) }
    var isEditing by remember { mutableStateOf(false) }

    fun startEditing() {
        lastName = initialLastName
        secondName = initialSecondName
        isEditing = true
    }

    fun cancelEditing() {
        lastName = initialLastName
        secondName = initialSecondName
        isEditing = false
        onCancelEdit()
    }

    fun saveEditing() {
        isEditing = false
        onSaveProfile(lastName.trim(), secondName.trim())
    }

    val profileBitmap = remember(profileImageBase64, selectedPhotoBytes) {
        val bytes = selectedPhotoBytes ?: runCatching {
            profileImageBase64?.let { Base64.decode(it, Base64.DEFAULT) }
        }.getOrNull()
        bytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    }

    val scroll = rememberScrollState()
    val bottomBarHeight = 160.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomBarHeight + 16.dp)
        ) {
            Spacer(Modifier.height(48.dp))

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
                        if (profileBitmap != null) {
                            Image(
                                bitmap = profileBitmap.asImageBitmap(),
                                contentDescription = "Profile photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            if (profileName.isNotBlank()) profileName else "Profile",
                            color = Color.White,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        Text(
                            if (email.isNotBlank()) email else "Email",
                            color = Color.White.copy(alpha = 0.8f),
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f)
                    .verticalScroll(scroll),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { if (isEditing) cancelEditing() else startEditing() }) {
                        Text(
                            if (isEditing) "Cancel" else "Edit profile",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    "First name",
                    color = Color.White,
                    modifier = Modifier.padding(start = 14.dp),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = initialFirstName,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = Color.White) },
                    singleLine = true,
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                        disabledTextColor = Color.White
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = NunitoFamily,
                        fontSize = 16.sp
                    ),
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    "Last name",
                    color = Color.White,
                    modifier = Modifier.padding(start = 14.dp),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    modifier = Modifier.fillMaxWidth(),
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

                Spacer(Modifier.height(20.dp))

                Text(
                    "Patronymic",
                    color = Color.White,
                    modifier = Modifier.padding(start = 14.dp),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = secondName,
                    onValueChange = { secondName = it },
                    modifier = Modifier.fillMaxWidth(),
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

                if (isEditing) {
                    Spacer(Modifier.height(16.dp))
                    TextButton(onClick = onPickPhoto) {
                        Text(
                            "Change photo",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { saveEditing() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(
                            "Save",
                            color = Color.Black,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(Modifier.height(22.dp))

                Text(
                    text = "My projects",
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 6.dp)
                )
                Spacer(Modifier.height(10.dp))

                myProjects.forEach { p ->
                    AccountProjectRow(title = p.name, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "My tasks",
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 6.dp)
                )
                Spacer(Modifier.height(10.dp))

                myTasks.forEach { t ->
                    AccountTaskRow(title = t.title, status = t.status, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(24.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(bottomBarHeight)
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
                    modifier = Modifier.clickable { onProjectsClick() }
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
                    modifier = Modifier.clickable { }
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

@Composable
private fun AccountProjectRow(
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFCBD5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = Color(0xFF2D5178)
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = title,
                color = Color.Black,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun AccountTaskRow(
    title: String,
    status: TaskStatus,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(status.dotColor().copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(status.dotColor())
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.Black,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = status.title,
                    color = status.dotColor(),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Preview(
    name = "Personal Account Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewPersonalAccount() {
    val sampleProjects = listOf(AccountProjectItem("1", "Arduino"))
    val sampleTasks = listOf(AccountTaskItem("1", "PLA-4", TaskStatus.TO_DO))

    PlannerTheme {
        PersonalAccountScreen(
            myProjects = sampleProjects,
            myTasks = sampleTasks,
            profileName = "Ivan Petrov",
            email = "ivan@example.com",
            initialFirstName = "Ivan",
            initialLastName = "Petrov",
            initialSecondName = "Sergeevich"
        )
    }
}

@Preview(
    name = "Personal Account Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewPersonalAccountDark() {
    PreviewPersonalAccount()
}
