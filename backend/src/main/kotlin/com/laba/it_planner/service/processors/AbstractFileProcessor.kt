package com.laba.it_planner.service.processors

import com.laba.it_planner.service.FileProcessor
import org.springframework.web.multipart.MultipartFile
import io.minio.MinioClient
import io.minio.PutObjectArgs
import java.util.*

abstract class AbstractFileProcessor : FileProcessor {

    protected fun getFileExtension(filename: String?): String {
        return if (filename?.contains(".") == true) {
            filename.substringAfterLast(".")
        } else {
            "bin"
        }
    }

    protected fun generateFileName(originalFilename: String?): String {
        val extension = getFileExtension(originalFilename)
        return "${UUID.randomUUID()}.$extension"
    }

    protected fun uploadToMinio(
        file: MultipartFile,
        minioClient: MinioClient,
        bucketName: String,
        path: String,
        contentType: String
    ) {
        minioClient.putObject(
            PutObjectArgs.builder()
                .contentType(contentType)
                .bucket(bucketName)
                .`object`(path)
                .stream(file.inputStream, file.size, -1)
                .build()
        )
    }

    override fun supports(file: MultipartFile): Boolean {
        val extension = getFileExtension(file.originalFilename).lowercase()
        return getSupportedExtensions().contains(extension)
    }
}