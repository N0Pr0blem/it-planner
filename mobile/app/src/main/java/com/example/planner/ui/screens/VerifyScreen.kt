package com.example.planner.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planner.ui.theme.BlueBackground
import com.example.planner.ui.theme.GreenButton
import com.example.planner.ui.theme.NunitoFamily
import com.example.planner.ui.theme.PlannerTheme
import androidx.compose.material3.Divider


@Composable
fun VerifyScreen(
    onVerify: (String) -> Unit = {},
    onResend: () -> Unit = {},
    isLoading: Boolean = false
) {
    var code by remember { mutableStateOf(List(4) { "" }) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // --- Заголовок ---
            Text(
                text = "Task Planner",
                color = Color.White,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 28.dp)
                    .shadow(2.dp, spotColor = Color.Black)
            )

            // --- Карточка ---
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter the code:",
                        color = Color.Black,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )

                    Spacer(Modifier.height(30.dp))

                    // --- 6 ячеек ---
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        code.forEachIndexed { index, value ->
                            OutlinedTextField(
                                value = value,
                                onValueChange = {
                                    if (it.length <= 1 && it.all(Char::isDigit)) {
                                        code = code.toMutableList().apply { set(index, it) }
                                    }
                                },
                                modifier = Modifier
                                    .width(52.dp)
                                    .height(62.dp)
                                    .background(Color(0xFFEAEAEA), RoundedCornerShape(6.dp)), // 👈 серый фон
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Black,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = Color.Black
                                ),
                                shape = RoundedCornerShape(6.dp)
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp, end = 6.dp, top = 20.dp),   // расстояние от ячеек
                        color = Color.Black,
                        thickness = 2.dp
                    )

                    Spacer(Modifier.height(40.dp))
                }
            }

            // --- Кнопка Verify ---
            Button(
                onClick = { onVerify(code.joinToString("")) },
                enabled = !isLoading && code.all { it.length == 1 },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(GreenButton),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier
                    .offset(y = (-24).dp)
                    .fillMaxWidth(0.4f)
                    .height(46.dp)
            ) {
                Text(
                    "Verify",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(20.dp))


            Text(
                text = "Resend code",
                color = if (isLoading) Color(0xFFBBBBBB) else Color.White,
                fontFamily = NunitoFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .shadow(2.dp, spotColor = Color.Black)
                    .then(
                        if (isLoading) {
                            Modifier
                        } else {
                            Modifier.clickable { onResend() }
                        }
                    )
            )
        }
    }
}

@Preview(
    name = "Verify Screen",
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFF1B3A5C,
    device = Devices.PIXEL_6
)
@Composable
fun PreviewVerifyScreen() {
    PlannerTheme {
        VerifyScreen()
    }
}
