package com.laba.it_planner.service.processors

import com.laba.it_planner.utils.files.MimeType
import com.laba.it_planner.utils.files.MimeType.*
import io.minio.MinioClient
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class DocxProcessor : AbstractFileProcessor() {
    override fun getSupportedExtensions(): List<MimeType> = listOf(
        DOC, DOCX, XLS, XLSX, PPT, PPTX, ODT
    )

    override fun process(file: MultipartFile, minioClient: MinioClient, bucketName: String, path: String) {
        val contentType = MimeType.getByExtension(getFileExtension(file.originalFilename).lowercase()).mime

        uploadToMinio(file, minioClient, bucketName, path, contentType)
    }
}