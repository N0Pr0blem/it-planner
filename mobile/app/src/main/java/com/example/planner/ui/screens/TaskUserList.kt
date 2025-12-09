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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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

@Immutable
data class ProjectMemberUi(
    val id: String,
    val name: String,
    val role: String,
)

@Composable
fun ProjectMembersScreen(
    projectName: String,
    members: List<ProjectMemberUi>,
    activeTab: ProjectTab = ProjectTab.Members,
    onTabChange: (ProjectTab) -> Unit = {},          // TODO
    onBack: () -> Unit = {},                         // TODO
    onAddMember: () -> Unit = {},                    // TODO
    onMemberClick: (ProjectMemberUi) -> Unit = {},   // TODO
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        // ВАЖНО: тут НЕТ horizontal padding — чтобы нижний бар был во всю ширину
        Column(modifier = Modifier.fillMaxSize()) {

            // Контент с отступами по бокам
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
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
                        text = projectName,
                        color = Color.White,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
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

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Members (${members.size})",
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    // маленький запас снизу (но не 120 — это уже делает бар)
                    contentPadding = PaddingValues(bottom = 18.dp)
                ) {
                    items(members, key = { it.id }) { m ->
                        MemberCard(
                            member = m,
                            onClick = { onMemberClick(m) }
                        )
                    }
                }
            }

            // Нижний бар — без боковых отступов, во всю ширину
            ProjectBottomBar(
                modifier = Modifier.fillMaxWidth(),
                activeTab = activeTab,
                onTabChange = onTabChange
            )
        }
    }
}

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
            .height(160.dp)
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
                onClick = { onTabChange(ProjectTab.Tasks) } // TODO
            )
            ProjectBottomItem(
                icon = Icons.Default.Groups,
                label = "Members",
                tint = navFg,
                active = activeTab == ProjectTab.Members,
                onClick = { onTabChange(ProjectTab.Members) } // TODO
            )
            ProjectBottomItem(
                icon = Icons.Default.Folder,
                label = "Repository",
                tint = navFg,
                active = activeTab == ProjectTab.Repository,
                onClick = { onTabChange(ProjectTab.Repository) } // TODO
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
            .padding(horizontal = 6.dp, vertical = 6.dp)
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

@Composable
private fun MemberCard(
    member: ProjectMemberUi,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCBD5F5)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.name.firstOrNull()?.uppercase() ?: "",
                    color = Color(0xFF1F2937),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.name,
                    color = Color.Black,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = member.role,
                    color = Color.Black.copy(alpha = 0.55f),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Open",
                tint = Color.Black.copy(alpha = 0.35f),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(
    name = "Project Members – Default",
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewProjectMembers() {
    PlannerTheme {
        ProjectMembersScreen(
            projectName = "Planner Mobile",
            members = listOf(
                ProjectMemberUi("1", "Olga Sliapitsa", "Backend Developer"),
                ProjectMemberUi("2", "Konstantin Vladimirovich", "Backend Developer"),
                ProjectMemberUi("3", "Джонатан Джозеф", "Backend Developer"),
            ),
            activeTab = ProjectTab.Members
        )
    }
}

@Preview(
    name = "Project Members – Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
private fun PreviewProjectMembersDark() {
    PlannerTheme { PreviewProjectMembers() }
}
