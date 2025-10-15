package com.laba.it_planner.service

import com.laba.it_planner.model.project.repository.FileType
import com.laba.it_planner.model.project.repository.ProjectRepoFile
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

interface RepoService {
    fun save(projectId: Long, file: MultipartFile, fileType: FileType, principal: Principal): String
    fun getAllRepositoryFiles(projectId: Long, principal: Principal): List<ProjectRepoFile>
    fun getFile(projectId: Long, fileId: Long, principal: Principal): ByteArray
}