package com.laba.it_planner.service.processors

import io.minio.MinioClient
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class DocxProcessor : AbstractFileProcessor() {
    override fun getSupportedExtensions(): List<String> = listOf("doc", "docx", "xls", "xlsx", "ppt", "pptx")

    override fun process(file: MultipartFile, minioClient: MinioClient, bucketName: String, path: String) {
        val contentType = when (getFileExtension(file.originalFilename).lowercase()) {
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "xls" -> "application/vnd.ms-excel"
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "ppt" -> "application/vnd.ms-powerpoint"
            "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            else -> "application/octet-stream"
        }

        uploadToMinio(file, minioClient, bucketName, path, contentType)
    }
}