package com.laba.it_planner.service.impl

import com.laba.it_planner.service.FileProcessorFactory
import com.laba.it_planner.service.FileService
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.RemoveObjectArgs
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

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

}