package com.laba.it_planner.service.storage.impl

import com.laba.it_planner.dto.storage.StorageFileDataDto
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.storage.Storage
import com.laba.it_planner.model.storage.StorageFile
import com.laba.it_planner.repository.storage.StorageFileRepository
import com.laba.it_planner.repository.storage.StorageRepository
import com.laba.it_planner.service.storage.FileService
import com.laba.it_planner.service.storage.StorageService
import com.laba.it_planner.utils.files.MimeType
import jakarta.transaction.Transactional
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.util.*

@Service
class StorageServiceImpl(
    private val storageRepository: StorageRepository,
    private val fileService: FileService,
    private val storageFileRepository: StorageFileRepository,
    private val messageSource: MessageSource,
) : StorageService {

    override fun createStorage(storage: Storage): Storage {
        return storageRepository.save(storage)
    }

    @Transactional
    override fun save(
        storage: Storage,
        file: MultipartFile
    ): StorageFile {
        val newFileName = fileService.saveFile(storage.path, file)
        val newFile = StorageFile(
            name = newFileName,
            storage = storage,
            creationDate = LocalDateTime.now(),
            mimeType = MimeType.getByFileName(newFileName),
        )

        return storageFileRepository.save(newFile)
    }

    override fun getFileInfo(storage: Storage, fileId: Long): StorageFileDataDto {
        val storageFile = getFile(storage, fileId)

        val fileData = getFileData(storage, fileId)
        val encodedFileName = URLEncoder.encode(storageFile.name, StandardCharsets.UTF_8)
            .replace("+", "%20")
        val size = fileData.size
        val mimeType = storageFile.mimeType

        return StorageFileDataDto(
            encodedFileName = encodedFileName,
            mimeType = mimeType.mime,
            size = size.toString(),
            content = fileData,
        )
    }

    fun getFile(storage: Storage, fileId: Long): StorageFile {
        val file = storage.files.stream()
            .filter { file -> file.id == fileId }
            .findFirst()
            .orElseThrow { DataException("error.storage.file.not_exist", fileId.toString()) }

        return file
    }

    override fun getFileData(storage: Storage, fileId: Long): ByteArray {
        val file = getFile(storage, fileId)
        val path = getAbsolutePath(storage, file)

        return fileService.getFile(path)
    }

    override fun deleteFile(storage: Storage, fileId: Long, locale: Locale): String {
        val file = getFile(storage, fileId)
        val path = getAbsolutePath(storage, file)

        storageFileRepository.deleteById(fileId)
        fileService.deleteFile(path)

        return messageSource.getMessage("message.storage.successfully_delete", arrayOf(fileId), locale)
    }

    private fun getAbsolutePath(storage: Storage, file: StorageFile): String {
        return "${storage.path}/${file.name}"
    }
}