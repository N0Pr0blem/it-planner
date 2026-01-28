package com.laba.it_planner.config

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
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

    @Value("\${minio.bucket-name}")
    private lateinit var bucketName: String

    @Bean
    fun minioClient(): MinioClient {
        println("Configuring MinIO with endpoint: $minioUrl")
        return MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(accessKey, secretKey)
            .region(region)
            .build()
    }

    @Bean
    fun minioBucketInitializer(minioClient: MinioClient): CommandLineRunner {
        return CommandLineRunner {
            try {
                val exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())
                if (!exists) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).region(region).build())
                    println("Created MinIO bucket: $bucketName")
                } else {
                    println("MinIO bucket already exists: $bucketName")
                }
            } catch (e: Exception) {
                println("Warning: Could not initialize MinIO bucket: ${e.message}")
                // Don't fail startup if MinIO is not available
            }
        }
    }
}