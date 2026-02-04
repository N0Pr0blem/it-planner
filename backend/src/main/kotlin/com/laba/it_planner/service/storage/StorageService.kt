package com.laba.it_planner.service.storage

import com.laba.it_planner.dto.storage.StorageFileDataDto
import com.laba.it_planner.model.storage.Storage
import com.laba.it_planner.model.storage.StorageFile
import org.springframework.web.multipart.MultipartFile
import java.util.Locale

interface StorageService {
    fun createStorage(storage: Storage): Storage

    fun save(storage: Storage, file: MultipartFile): StorageFile

    fun getFileInfo(storage: Storage, fileId: Long): StorageFileDataDto

    fun getFileData(storage: Storage, fileId: Long): ByteArray

    fun deleteFile(storage: Storage, fileId: Long, locale: Locale): String
}