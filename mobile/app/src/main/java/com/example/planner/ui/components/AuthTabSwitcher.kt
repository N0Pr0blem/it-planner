package com.example.planner.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.draw.clip

@Composable
fun AuthTabSwitcher(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    angleDeg: Float = -22f,            // угол наклона «пилюли»
    pillWidth: Dp = 130.dp,            // ширина «пилюли»
    pillHeight: Dp = 94.dp,            // высота «пилюли»
    pillColor: Color = Color(0xFF2D5178),
    pillInactiveAlpha: Float = 0.30f
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp,  bottom = 32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("Login", "Register").forEachIndexed { i, tab ->
            val isSelected = tab == selectedTab
            val bg = animateColorAsState(
                targetValue = if (isSelected) pillColor else pillColor.copy(alpha = pillInactiveAlpha),
                label = "tabBg"
            ).value

            // Контейнер одной вкладки
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .height(pillHeight) // выравниваем по высоте
                    .wrapContentWidth()
            ) {
                // НАКЛОНЁННЫЙ ФОН (только фон, текст не вращается)
                Box(
                    modifier = Modifier
                        .size(pillWidth, pillHeight)
                        .graphicsLayer { rotationZ = angleDeg } // наклоняем фон
                        .align(Alignment.Center)
                        .background(bg, CircleShape)
                )

                // Кликабельная "чипка" поверх — горизонтальная
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Tab
                        ) { if (!isSelected) onTabSelected(tab) }
                        .padding(horizontal = 28.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
