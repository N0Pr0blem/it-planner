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
                    LoginScreen { tab -> currentTab = tab }
                else
                    RegisterScreen { tab -> currentTab = tab }
            }
        }
    }
}
