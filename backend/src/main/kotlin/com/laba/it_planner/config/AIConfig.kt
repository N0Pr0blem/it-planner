package com.laba.it_planner.config

import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

@Configuration
class AIConfig(
    @Value("\${ai.openai.api.key}")
    var openAiApiKey: String,

    @Value("\${ai.openai.api.model}")
    var openAiApiModel: String,

    @Value("\${ai.openai.api.url}")
    var openAiApiUrl: String,

    @Value("\${ai.minRequestLength}")
    var minRequestLength: Int,
){
    @Bean
    fun client(): OkHttpClient{
        return OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 8081)))
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .callTimeout(90, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }
}

