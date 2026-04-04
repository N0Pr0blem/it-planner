package com.laba.it_planner.controller.storage

import com.laba.it_planner.controller.project.ProjectController
import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.storage.StorageFileDto
import com.laba.it_planner.mapper.storage.StorageFileMapper
import com.laba.it_planner.model.storage.Storage
import com.laba.it_planner.service.project.ProjectService
import com.laba.it_planner.service.storage.StorageService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.util.*
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/storage/{storageId}")
class StorageController(
    private val storageService: StorageService,
    private val projectService: ProjectService,
    private val storageFileMapper: StorageFileMapper,
) {
    private val logger: Logger = Logger.getLogger(StorageController::class.java.name)

    @ModelAttribute("storage")
    fun getStorageAttribute(
        @PathVariable storageId: Long,
        principal: Principal
    ): Storage = projectService.getStorage(storageId, principal)

    @PostMapping
    @Operation(description = "Save file")
    fun save(
        @ModelAttribute("storage") storage: Storage,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<StorageFileDto> {
        logger.info("EVENT_SAVE_FILE | Start saving file")
        val res = storageService.save(storage, file)
        logger.info("EVENT_SAVE_FILE | End saving file")
        return ResponseEntity.ok(storageFileMapper.toDto(res))
    }

    @GetMapping("/file/{fileId}")
    @Operation(description = "Get storage file")
    fun downloadFile(
        @ModelAttribute("storage") storage: Storage,
        @PathVariable("fileId") fileId: Long
    ): ResponseEntity<ByteArray> {
        logger.info("EVENT_GET_FILE | Start getting file")
        val file = storageService.getFileInfo(storage, fileId)
        logger.info("EVENT_GET_FILE | End getting file")

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(file.mimeType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${file.encodedFileName}\"")
            .header(HttpHeaders.CONTENT_LENGTH, file.size)
            .body(file.content)
    }

    @DeleteMapping("/file/{fileId}")
    @Operation(description = "Delete repository file")
    fun delete(
        @ModelAttribute("storage") storage: Storage,
        @PathVariable("fileId") fileId: Long,
        locale: Locale
    ): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_DELETE_FILE | Start deleting file")
        val result = storageService.deleteFile(storage, fileId, locale)
        logger.info("EVENT_DELETE_FILE | End deleting file")
        return ResponseEntity.ok(MessageResponseDto(result))
    }

    @GetMapping
    @Operation(description = "Get all repository files")
    fun getAll(
        @ModelAttribute("storage") storage: Storage
    ): ResponseEntity<List<StorageFileDto>> {
        logger.info("EVENT_GET_ALL_FILES | Start getting all files")
        val files = storage.files?:emptyList();
        logger.info("EVENT_GET_ALL_FILES | End getting all files")
        return ResponseEntity.ok(storageFileMapper.toDtos(files))
    }
}