package com.laba.it_planner.service

import io.minio.MinioClient
import org.springframework.web.multipart.MultipartFile

interface FileProcessor {
    fun supports(file: MultipartFile): Boolean
    fun process(file: MultipartFile, minioClient: MinioClient, bucketName: String, path: String)
    fun getSupportedExtensions(): List<String>
}