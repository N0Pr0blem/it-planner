package com.laba.it_planner.service.processors

import io.minio.MinioClient
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class PDFProcessor : AbstractFileProcessor() {
    override fun getSupportedExtensions(): List<String> = listOf("pdf")

    override fun process(file: MultipartFile, minioClient: MinioClient, bucketName: String, path: String) {
        validatePdf(file)

        uploadToMinio(file, minioClient, bucketName, path, "application/pdf")
    }

    private fun validatePdf(file: MultipartFile) {
        val bytes = ByteArray(4)
        file.inputStream.use { stream ->
            stream.read(bytes)
        }

        if (!bytes.contentEquals(byteArrayOf(0x25, 0x50, 0x44, 0x46))) {
            throw IllegalArgumentException("Invalid PDF file")
        }
    }
}