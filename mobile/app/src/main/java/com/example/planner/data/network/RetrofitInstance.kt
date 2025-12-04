package com.example.planner.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Moshi для парсинга JSON
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Логгер для отладки сетевых запросов в Logcat (очень полезно!)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttp клиент с логгером
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        // Здесь в будущем нужно будет добавить Interceptor для подстановки токена авторизации
        .build()

    // Экземпляр Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    // Ленивое создание экземпляра ApiService
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
