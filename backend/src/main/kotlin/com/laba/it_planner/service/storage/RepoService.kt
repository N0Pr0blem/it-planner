package com.laba.it_planner.service

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.repo.ProjectRepoFileDto
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

interface RepoService {
    fun save(projectId: Long, file: MultipartFile, principal: Principal): MessageResponseDto
    fun getAllRepositoryFiles(projectId: Long, principal: Principal): List<ProjectRepoFileDto>
    fun getFile(projectId: Long, fileId: Long, principal: Principal): ByteArray
    fun getFileName(fileId: Long): String
    fun delete(projectId: Long, fileId: Long, principal: Principal): String
}