package com.laba.it_planner.service

import com.laba.it_planner.utils.files.MimeType
import io.minio.MinioClient
import org.springframework.web.multipart.MultipartFile

interface FileProcessor {
    fun supports(file: MultipartFile): Boolean
    fun process(file: MultipartFile, minioClient: MinioClient, bucketName: String, path: String)
    fun getSupportedExtensions(): List<MimeType>
    fun generateFileName(originalFilename: String?): String
}