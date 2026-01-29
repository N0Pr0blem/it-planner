package com.laba.it_planner.service.storage.processors

import com.laba.it_planner.utils.files.MimeType
import com.laba.it_planner.utils.files.MimeType.*
import io.minio.MinioClient
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class TextProcessor : AbstractFileProcessor() {

    override fun getSupportedExtensions(): List<MimeType> = listOf(
        TXT,
        MARKDOWN,
        CSV,
        TSV,
        JSON,
        XML,
        YAML,
        HTML,
        CSS,
        JAVASCRIPT,
        TYPESCRIPT,
        JAVA,
        KOTLIN,
        PYTHON,
        C,
        CPP,
        PHP,
        RUBY,
        GO,
        RUST,
        SQL
    )

    override fun process(file: MultipartFile, minioClient: MinioClient, bucketName: String, path: String) {
        val contentType = MimeType.getByExtension(getFileExtension(file.originalFilename).lowercase()).mime

        uploadToMinio(file, minioClient, bucketName, path, contentType)
    }
}