package com.example.planner.data.network

import android.content.Context
import android.content.SharedPreferences
import java.util.Date

object TokenManager {
    private const val PREFS_NAME = "planner_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_EXPIRES_AT = "token_expires_at"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String, expiresAt: Date?) {
        prefs?.edit()?.apply {
            putString(KEY_TOKEN, token)
            putLong(KEY_EXPIRES_AT, expiresAt?.time ?: 0L)
            apply()
        }
    }

    fun getToken(): String? {
        return prefs?.getString(KEY_TOKEN, null)
    }

    fun isTokenValid(): Boolean {
        val token = getToken() ?: return false
        val expiresAt = prefs?.getLong(KEY_EXPIRES_AT, 0L) ?: 0L
        return token.isNotEmpty() && (expiresAt == 0L || expiresAt > System.currentTimeMillis())
    }

    fun clearToken() {
        prefs?.edit()?.apply {
            remove(KEY_TOKEN)
            remove(KEY_EXPIRES_AT)
            apply()
        }
    }
}
