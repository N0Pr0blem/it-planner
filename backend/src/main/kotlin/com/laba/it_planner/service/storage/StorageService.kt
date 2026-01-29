package com.laba.it_planner.service.storage

import com.laba.it_planner.model.storage.Storage
import com.laba.it_planner.model.storage.StorageFile
import org.springframework.web.multipart.MultipartFile

interface StorageService {
    fun createStorage(storage: Storage): Storage

    fun save(storage: Storage, file: MultipartFile): StorageFile

    fun getFileData(storage: Storage, fileId: Long): ByteArray

    fun getFileName(fileId: Long): String

    fun deleteFile(storage: Storage, fileId: Long)
}