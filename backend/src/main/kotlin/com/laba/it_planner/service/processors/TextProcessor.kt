package com.laba.it_planner.service.processors

import io.minio.MinioClient
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class TextProcessor : AbstractFileProcessor() {

    override fun getSupportedExtensions(): List<String> = listOf(
        "txt", "text", "log",
        "md", "markdown",
        "csv", "tsv",
        "json", "xml", "yml", "yaml",
        "html", "htm", "css", "js",
        "java", "kt", "py", "cpp", "c", "h",
        "php", "rb", "go", "rs", "sql"
    )

    override fun process(file: MultipartFile, minioClient: MinioClient, bucketName: String, path: String) {
        val extension = getFileExtension(file.originalFilename).lowercase()
        val contentType = when (extension) {
            "txt", "text", "log" -> "text/plain"
            "md", "markdown" -> "text/markdown"
            "csv" -> "text/csv"
            "tsv" -> "text/tab-separated-values"
            "json" -> "application/json"
            "xml" -> "application/xml"
            "yml", "yaml" -> "application/x-yaml"
            "html", "htm" -> "text/html"
            "css" -> "text/css"
            "js" -> "application/javascript"
            "java" -> "text/x-java-source"
            "kt" -> "text/x-kotlin"
            "py" -> "text/x-python"
            "cpp", "c", "h" -> "text/x-c"
            "php" -> "application/x-php"
            "rb" -> "application/x-ruby"
            "go" -> "text/x-go"
            "rs" -> "text/x-rust"
            "sql" -> "application/sql"
            else -> "text/plain"
        }

        uploadToMinio(file, minioClient, bucketName, path, contentType)
    }
}