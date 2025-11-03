package com.laba.it_planner.service.processors

import org.springframework.web.multipart.MultipartFile
import io.minio.MinioClient
import org.springframework.stereotype.Component

@Component
class ImageProcessor : AbstractFileProcessor() {
    override fun getSupportedExtensions(): List<String> = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")

    override fun process(file: MultipartFile, minioClient: MinioClient, bucketName: String, path: String) {
        if (file.contentType?.startsWith("image/") != true) {
            throw IllegalArgumentException("File is not a valid image")
        }

        validateImage(file)
        uploadToMinio(file, minioClient, bucketName, path, file.contentType ?: "image/jpeg")
    }

    private fun validateImage(file: MultipartFile) {
        if (file.size > 10 * 1024 * 1024) { // 10MB
            throw IllegalArgumentException("Image size too large")
        }
    }
}