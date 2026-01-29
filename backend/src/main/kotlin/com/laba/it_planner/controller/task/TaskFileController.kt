package com.laba.it_planner.controller.task

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.task.TaskFileDto
import com.laba.it_planner.service.TaskFileService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.Principal

@RestController
@RequestMapping("/api/v1/task/{taskId}/file")
class TaskFileController(
    private val taskFileService: TaskFileService
) {
    @PostMapping
    @Operation(description = "Save task file")
    fun save(
        @PathVariable("taskId") taskId: Long,
        @RequestPart("file") file: MultipartFile,
        principal: Principal
    ): ResponseEntity<MessageResponseDto> {
        return ResponseEntity.ok(taskFileService.save(file, taskId, principal))
    }

    @GetMapping
    @Operation(description = "Get all repository files")
    fun getAll(
        @PathVariable("taskId") taskId: Long,
        principal: Principal
    ): ResponseEntity<List<TaskFileDto>> {
        return ResponseEntity.ok(taskFileService.getAllTasksFiles(taskId, principal))
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "Delete task file")
    fun delete(
        @PathVariable("taskId") taskId: Long,
        @PathVariable("fileId") fileId: Long,
    ): ResponseEntity<MessageResponseDto> {
        return ResponseEntity.ok(MessageResponseDto(taskFileService.delete(fileId)))
    }

    @GetMapping("/{fileId}")
    @Operation(description = "Get task file")
    fun downloadFile(
        @PathVariable("taskId") projectId: Long,
        @PathVariable("fileId") fileId: Long,
        principal: Principal
    ): ResponseEntity<ByteArray> {
        val file = taskFileService.getFile(projectId, fileId, principal)
        val fileName = taskFileService.getFileName(fileId)
        val mimeType = taskFileService.getMimeType(fileName)

        val encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
            .replace("+", "%20")


        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(mimeType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$encodedFileName\"")
            .header(HttpHeaders.CONTENT_LENGTH, file.size.toString())
            .body(file)
    }
}