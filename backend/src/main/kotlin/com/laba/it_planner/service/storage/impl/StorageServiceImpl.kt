package com.laba.it_planner.service.storage

import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.storage.Storage
import com.laba.it_planner.model.storage.StorageFile
import com.laba.it_planner.repository.storage.StorageFileRepository
import com.laba.it_planner.repository.storage.StorageRepository
import com.laba.it_planner.service.FileService
import com.laba.it_planner.service.StorageService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class StorageServiceImpl(
    private val storageRepository: StorageRepository,
    private val fileService: FileService,
    private val storageFileRepository: StorageFileRepository
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
        )

        return storageFileRepository.save(newFile)
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
        val path = getAbsolutePath(storage,file)

        return fileService.getFile(path)
    }

    override fun getFileName(fileId: Long): String {
        return storageFileRepository.findById(fileId).get().name ?: ""
    }

    override fun deleteFile(storage: Storage, fileId: Long) {
        val file = getFile(storage, fileId)
        val path = getAbsolutePath(storage,file)

        storageFileRepository.deleteById(fileId)
        fileService.deleteFile(path)
    }

    private fun getAbsolutePath(storage: Storage, file: StorageFile): String {
        return "${storage.path}/${file.name}"
    }
}