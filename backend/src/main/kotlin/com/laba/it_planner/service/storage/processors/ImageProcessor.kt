package com.laba.it_planner.service.storage.processors

import com.laba.it_planner.utils.files.MimeType
import com.laba.it_planner.utils.files.MimeType.*
import io.minio.MinioClient
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ImageProcessor : AbstractFileProcessor() {
    val size: Int = 50 * 1024 * 1024

    override fun getSupportedExtensions(): List<MimeType> = listOf(
        JPEG, PNG, GIF, BMP, WEBP, SVG, ICO, TIFF, HEIC, AVIF
    )

    override fun process(file: MultipartFile, minioClient: MinioClient, bucketName: String, path: String) {
        if (file.contentType?.startsWith("image/") != true) {
            throw IllegalArgumentException("File is not a valid image")
        }

        validateImage(file)
        uploadToMinio(file, minioClient, bucketName, path, file.contentType ?: "image/jpeg")
    }

    private fun validateImage(file: MultipartFile) {
        if (file.size > size) { // 50MB
            throw IllegalArgumentException("Image size too large")
        }
    }
}