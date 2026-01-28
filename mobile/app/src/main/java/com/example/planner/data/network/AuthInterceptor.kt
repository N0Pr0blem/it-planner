package com.example.planner.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Для запросов на /auth/login и /auth/register не добавляем токен
        val path = originalRequest.url.encodedPath
        val isAuthRequest = path.contains("/auth/login") ||
            path.contains("/auth/register") ||
            path.contains("/auth/verify") ||
            path.contains("/auth/resend")
        
        return if (isAuthRequest) {
            // Для аутентификации просто пропускаем запрос без токена
            chain.proceed(originalRequest)
        } else {
            // Для остальных запросов добавляем токен, если он есть
            val token = TokenManager.getToken()
            if (token != null) {
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(originalRequest)
            }
        }
    }
}
