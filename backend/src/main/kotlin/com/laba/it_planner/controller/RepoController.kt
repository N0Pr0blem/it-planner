package com.laba.it_planner.controller

import com.laba.it_planner.model.project.repository.FileType
import com.laba.it_planner.model.project.repository.ProjectRepoFile
import com.laba.it_planner.service.RepoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/api/v1/project/{projectId}/repository")
class RepoController(
    private val repoService: RepoService
) {
    @PostMapping
    @Operation(description = "Save repository file")
    fun save(
        @PathVariable("projectId") projectId: Long,
        @RequestPart("multipartFile") file: MultipartFile,
        principal: Principal
    ): ResponseEntity<String>{
        return ResponseEntity.ok(repoService.save(projectId,file, FileType.REPOSITORY,principal))
    }

    @GetMapping("/file/{fileId}")
    @Operation(description = "Get repository file")
    fun get(
        @PathVariable("projectId") projectId: Long,
        @PathVariable("fileId") fileId: Long,
        principal: Principal
    ): ResponseEntity<ByteArray>{
        return ResponseEntity.ok(repoService.getFile(projectId,fileId,principal))
    }

    @GetMapping
    @Operation(description = "Get all repository files")
    fun getAll(
        @PathVariable("projectId") projectId: Long,
        principal: Principal
    ): ResponseEntity<List<ProjectRepoFile>>{
        return ResponseEntity.ok(repoService.getAllRepositoryFiles(projectId,principal))
    }
}