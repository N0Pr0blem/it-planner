package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.GreenButton
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.theme.PlannerTheme

@Immutable
data class RepoFileUi(
    val id: String,
    val filename: String,
)

/**
 * Экран "Repository" (файлы проекта) — чистая верстка.
 *
 * Внизу: проектное меню 3 вкладки (Tasks / Members / Repository).
 * Навигации нет — все действия через TODO колбэки.
 *
 * ВАЖНО: ProjectTab должен быть объявлен ОДИН раз в проекте (Tasks/Members/Repository),
 * иначе будет Redeclaration.
 */
@Composable
fun ProjectRepositoryScreen(
    projectName: String,
    files: List<RepoFileUi>,
    activeTab: ProjectTab = ProjectTab.Repository,
    onTabChange: (ProjectTab) -> Unit = {},

    onBack: () -> Unit = {},                 // TODO: назад к проекту/списку
    onAddMember: () -> Unit = {},            // TODO: открыть экран добавления участника
    onFileClick: (RepoFileUi) -> Unit = {},  // TODO: открыть файл
    onDeleteFile: (RepoFileUi) -> Unit = {}, // TODO: удалить файл

    onPickFile: () -> Unit = {},             // TODO: открыть системный picker
    onUploadFile: (String) -> Unit = {},     // TODO: загрузить выбранный файл (передай имя/uri как решите)
) {
    val bottomBarHeight = 160.dp

    var query by remember { mutableStateOf("") }
    var showUploadDialog by remember { mutableStateOf(false) }
    var selectedFilename by remember { mutableStateOf<String?>(null) }

    var deleteConfirmFor by remember { mutableStateOf<RepoFileUi?>(null) }

    val filtered = remember(query, files) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) files
        else files.filter { it.filename.lowercase().contains(q) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        // ===== Контент (скролл) =====
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 18.dp,
                bottom = bottomBarHeight + 22.dp // запас под нижний полукруг
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ---- Top bar ----
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = projectName,
                        color = Color.White,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    IconButton(
                        onClick = onAddMember,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Add member",
                            tint = Color.White
                        )
                    }
                }
            }

            // ---- Title ----
            item {
                Text(
                    text = "Repository",
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // ---- Search ----
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF2D5178))
                    },
                    placeholder = {
                        Text(
                            "Search files…",
                            fontFamily = NunitoFamily,
                            color = Color(0xFF2D5178).copy(alpha = 0.55f)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color(0xFF111827),
                        unfocusedTextColor = Color(0xFF111827),
                        cursorColor = Color(0xFF111827),
                    )
                )
            }

            // ---- Files list ----
            items(filtered, key = { it.id }) { f ->
                RepoFileCard(
                    file = f,
                    onOpen = { onFileClick(f) },
                    onDelete = { deleteConfirmFor = f }
                )
            }

            if (filtered.isEmpty()) {
                item {
                    Text(
                        text = "No files yet",
                        color = Color.White.copy(alpha = 0.75f),
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }

        // ===== FAB (+) =====
        FloatingActionButton(
            onClick = {
                // TODO: открыть диалог загрузки
                showUploadDialog = true
                selectedFilename = null
            },
            containerColor = GreenButton,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = bottomBarHeight + 16.dp)
                .size(58.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add file", tint = Color.White)
        }

        // ===== Нижнее меню (полная ширина, НЕ зависит от padding контента) =====
        ProjectBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(bottomBarHeight),
            activeTab = activeTab,
            onTabChange = onTabChange
        )

        // ===== Диалог "Upload file" =====
        if (showUploadDialog) {
            UploadFileDialog(
                selectedFilename = selectedFilename,
                onPick = {
                    // TODO: открыть file picker
                    onPickFile()

                    // демо-заглушка, чтобы было видно состояние выбора:
                    if (selectedFilename == null) selectedFilename = "english cv.docx"
                },
                onCancel = {
                    showUploadDialog = false
                    selectedFilename = null
                },
                onUpload = {
                    val name = selectedFilename ?: return@UploadFileDialog
                    // TODO: загрузить файл в проект
                    onUploadFile(name)
                    showUploadDialog = false
                    selectedFilename = null
                },
                onClose = {
                    showUploadDialog = false
                    selectedFilename = null
                }
            )
        }

        // ===== Confirm delete =====
        val toDelete = deleteConfirmFor
        if (toDelete != null) {
            ConfirmDeleteDialog(
                filename = toDelete.filename,
                onCancel = { deleteConfirmFor = null },
                onConfirm = {
                    // TODO: удалить файл
                    onDeleteFile(toDelete)
                    deleteConfirmFor = null
                }
            )
        }
    }
}

@Composable
private fun RepoFileCard(
    file: RepoFileUi,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpen)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFCBD5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.InsertDriveFile,
                    contentDescription = null,
                    tint = Color(0xFF2D5178)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.filename,
                    color = Color.Black,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = fileTypeLabel(file.filename),
                    color = Color.Black.copy(alpha = 0.55f),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFEF4444)
                )
            }
        }
    }
}

private fun fileTypeLabel(filename: String): String {
    val ext = filename.substringAfterLast('.', "").trim()
    return if (ext.isNotEmpty()) "${ext.uppercase()} file" else "File"
}

// ====== Upload dialog ======

@Composable
private fun UploadFileDialog(
    selectedFilename: String?,
    onPick: () -> Unit,
    onCancel: () -> Unit,
    onUpload: () -> Unit,
    onClose: () -> Unit,
) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upload file",
                        color = Color(0xFF14532D),
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF6B7280))
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "File",
                    color = Color(0xFF14532D),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                    elevation = CardDefaults.cardElevation(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onPick)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = Color(0xFF16A34A)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = selectedFilename ?: "Choose file…",
                            color = Color(0xFF111827).copy(alpha = if (selectedFilename == null) 0.45f else 1f),
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel", fontFamily = NunitoFamily, fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = onUpload,
                        enabled = selectedFilename != null,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenButton),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("Upload", fontFamily = NunitoFamily, fontWeight = FontWeight.Medium, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfirmDeleteDialog(
    filename: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Delete file?",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = filename,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel", fontFamily = NunitoFamily, fontWeight = FontWeight.Medium)
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("Delete", fontFamily = NunitoFamily, fontWeight = FontWeight.Medium, color = Color.White)
                    }
                }
            }
        }
    }
}

// ====== Bottom bar (project) ======

@Composable
private fun ProjectBottomBar(
    modifier: Modifier = Modifier,
    activeTab: ProjectTab,
    onTabChange: (ProjectTab) -> Unit
) {
    val navBg = Color(0xFFE8E8E8)
    val navFg = Color(0xFF2D5178)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp))
            .background(navBg),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectBottomItem(
                icon = Icons.Default.List,
                label = "Tasks",
                tint = navFg,
                active = activeTab == ProjectTab.Tasks,
                onClick = { onTabChange(ProjectTab.Tasks) }
            )
            ProjectBottomItem(
                icon = Icons.Default.Groups,
                label = "Members",
                tint = navFg,
                active = activeTab == ProjectTab.Members,
                onClick = { onTabChange(ProjectTab.Members) }
            )
            ProjectBottomItem(
                icon = Icons.Default.Folder,
                label = "Repository",
                tint = navFg,
                active = activeTab == ProjectTab.Repository,
                onClick = { onTabChange(ProjectTab.Repository) }
            )
        }
    }
}

@Composable
private fun ProjectBottomItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    active: Boolean,
    onClick: () -> Unit
) {
    val alpha = if (active) 1f else 0.7f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint.copy(alpha = alpha),
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            color = tint.copy(alpha = alpha),
            fontFamily = NunitoFamily,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

// ===== PREVIEW =====

@Preview(
    name = "Project Repository – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewProjectRepository() {
    PlannerTheme {
        // Для превью нужен ProjectTab в проекте (общий enum)
        ProjectRepositoryScreen(
            projectName = "Planner Mobile",
            files = listOf(
                RepoFileUi("1", "english cv.docx"),
                RepoFileUi("2", "api_contract.pdf"),
            ),
            activeTab = ProjectTab.Repository
        )
    }
}

@Preview(
    name = "Project Repository – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewProjectRepositoryDark() {
    PlannerTheme { PreviewProjectRepository() }
}
