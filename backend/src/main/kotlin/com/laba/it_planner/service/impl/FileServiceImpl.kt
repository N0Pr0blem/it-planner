package com.laba.it_planner.service.impl

import com.laba.it_planner.service.FileProcessorFactory
import com.laba.it_planner.service.FileService
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.RemoveObjectArgs
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.security.Principal

@Service
class FileServiceImpl(
    private val minioClient: MinioClient,
    private val fileProcessorFactory: FileProcessorFactory,
    @Value("\${minio.bucket-name}") private val bucketName: String
) : FileService {

    override fun saveFile(path: String, file: MultipartFile): String {
        val processor = fileProcessorFactory.getProcessor(file)

        processor.process(file, minioClient, bucketName, path)

        return path
    }

    override fun getFile(path: String): ByteArray {
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(path)
                .build()
        ).readAllBytes()
    }

    override fun deleteFile(path: String) {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .`object`(path)
                .build()
        )
    }

    override fun createDescriptionFileForTask(
        projectName: String,
        principal: Principal,
        taskUUID: String,
        description: String
    ): String {
        val minioPath = "projects/$projectName/$taskUUID/description.txt"

        val tmpDir = File("tmp")
        if (!tmpDir.exists()) tmpDir.mkdirs()

        val tempFile = File(tmpDir, "description_${taskUUID}_${System.currentTimeMillis()}.txt")

        try {
            tempFile.writeText(description, Charsets.UTF_8)
            val multipartFile: MultipartFile = SimpleMultipartFile(tempFile)
            return saveFile(minioPath, multipartFile)
        } finally {
            tempFile.delete()
        }
    }

    override fun updateFile(descriptionFile: String?, description: String?): String {
        if (descriptionFile == null || description == null) {
            return ""
        }

        try {
            val tmpDir = File("tmp")
            if (!tmpDir.exists()) tmpDir.mkdirs()

            val tempFile = File(tmpDir, "update_${System.currentTimeMillis()}.txt")

            try {
                tempFile.writeText(description, Charsets.UTF_8)
                val multipartFile = SimpleMultipartFile(tempFile)

                return saveFile(descriptionFile, multipartFile)

            } finally {
                tempFile.delete()
            }
        } catch (e: Exception) {
            throw RuntimeException("Ошибка при обновлении файла: ${e.message}", e)
        }
    }

    private class SimpleMultipartFile(private val file: File) : MultipartFile {

        override fun getName(): String = "file"
        override fun getOriginalFilename(): String? = "description.txt"
        override fun getContentType(): String? = "text/plain"
        override fun isEmpty(): Boolean = file.length() == 0L
        override fun getSize(): Long = file.length()

        override fun getBytes(): ByteArray {
            return file.readBytes()
        }

        override fun getInputStream(): java.io.InputStream {
            return file.inputStream()
        }

        override fun transferTo(dest: File) {
            file.inputStream().use { input ->
                dest.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}

