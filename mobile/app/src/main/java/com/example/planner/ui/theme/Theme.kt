package com.example.planner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.planner.R
import androidx.compose.ui.text.font.Font

private val LightColors = lightColorScheme(
    primary = BlueBackground,
    secondary = GreenButton,
    background = BlueBackground,
    onPrimary = White,
    onBackground = White
)

private val NunitoFamily = FontFamily(
    Font(R.font.nunito_black,  FontWeight.Normal),
)

private val AppTypography = Typography(
    // Заголовок карточки
    titleMedium = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    // Текст полей/лейблов/кнопок
    bodyMedium = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),
    labelLarge = TextStyle( // текст на кнопке
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
)

@Composable
fun PlannerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
