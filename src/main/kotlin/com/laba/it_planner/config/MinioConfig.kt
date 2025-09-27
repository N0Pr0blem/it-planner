package com.laba.it_planner.config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig {

    @Value("\${minio.endpoint}")
    private lateinit var minioUrl: String

    @Value("\${minio.access-key}")
    private lateinit var accessKey: String

    @Value("\${minio.secret-key}")
    private lateinit var secretKey: String

    @Value("\${minio.region}")
    private lateinit var region: String

    @Bean
    fun minioClient(): MinioClient {
        println("Configuring MinIO with endpoint: $minioUrl")
        return MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(accessKey, secretKey)
            .region(region)
            .build()
    }
}