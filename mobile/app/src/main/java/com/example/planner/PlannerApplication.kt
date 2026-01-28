package com.example.planner

import android.app.Application
import com.example.planner.data.network.TokenManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlannerApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize TokenManager
        TokenManager.init(this)
    }
}
