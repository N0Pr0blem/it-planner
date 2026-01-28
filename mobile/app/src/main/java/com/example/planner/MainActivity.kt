package com.example.planner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.planner.ui.navigation.AppNavHost
import com.example.planner.ui.navigation.NavRoutes
import com.example.planner.ui.theme.PlannerTheme
import com.example.planner.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannerTheme {
                val authViewModel: AuthViewModel = hiltViewModel()
                val authState by authViewModel.uiState.collectAsState()
                val startDestination = if (authState.isLoggedIn) {
                    NavRoutes.Projects.route
                } else {
                    NavRoutes.Login.route
                }

                val navController = rememberNavController()

                AppNavHost(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}
