package com.laba.it_planner.controller

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.repo.ProjectRepoFileDto
import com.laba.it_planner.model.project.repository.FileType
import com.laba.it_planner.service.RepoService
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
@RequestMapping("/api/v1/project/{projectId}/repository")
class RepoController(
    private val repoService: RepoService,
) {
    @PostMapping
    @Operation(description = "Save repository file")
    fun save(
        @PathVariable("projectId") projectId: Long,
        @RequestPart("file") file: MultipartFile,
        principal: Principal
    ): ResponseEntity<MessageResponseDto>{
        return ResponseEntity.ok(repoService.save(projectId,file, FileType.REPOSITORY,principal))
    }

    @GetMapping("/file/{fileId}")
    @Operation(description = "Get repository file")
    fun downloadFile(
        @PathVariable("projectId") projectId: Long,
        @PathVariable("fileId") fileId: Long,
        principal: Principal
    ): ResponseEntity<ByteArray> {
        val file = repoService.getFile(projectId, fileId, principal)
        val fileName = repoService.getFileName(fileId)
        val mimeType = repoService.getMimeType(fileName)

        val encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
            .replace("+", "%20")


        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(mimeType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$encodedFileName\"")
            .header(HttpHeaders.CONTENT_LENGTH, file.size.toString())
            .body(file)
    }

    @DeleteMapping("/file/{fileId}")
    @Operation(description = "Get repository file")
    fun delete(
        @PathVariable("projectId") projectId: Long,
        @PathVariable("fileId") fileId: Long,
        principal: Principal
    ): ResponseEntity<MessageResponseDto> {
        val result = repoService.delete(projectId, fileId, principal)
        return ResponseEntity.ok(MessageResponseDto(result))
    }

    @GetMapping
    @Operation(description = "Get all repository files")
    fun getAll(
        @PathVariable("projectId") projectId: Long,
        principal: Principal
    ): ResponseEntity<List<ProjectRepoFileDto>>{
        return ResponseEntity.ok(repoService.getAllRepositoryFiles(projectId,principal))
    }
}