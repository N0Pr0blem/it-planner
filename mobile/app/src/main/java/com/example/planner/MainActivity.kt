package com.example.planner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.planner.ui.screens.LoginScreen
import com.example.planner.ui.screens.RegisterScreen
import com.example.planner.ui.theme.PlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannerTheme {
                var currentTab by remember { mutableStateOf("Login") }

                if (currentTab == "Login")
                    LoginScreen (
                        onTabSwitch = { tab -> currentTab = tab },
                        onLogin = { _, _ -> },
                        isLoading = false,
                        error = null,
                        onErrorDismiss = {}
                    )
                else
                    RegisterScreen(
                        onTabSwitch = { tab -> currentTab = tab },
                        onRegister = { _, _, _ -> },
                        isLoading = false,
                        error = null,
                        onErrorDismiss = {}
                    )
            }
        }
    }
}
