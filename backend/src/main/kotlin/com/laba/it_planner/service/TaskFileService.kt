package com.laba.it_planner.service

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.task.TaskFileDto
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

interface TaskFileService {
    fun save(file: MultipartFile, taskId: Long, principal: Principal): MessageResponseDto?
    fun getAllTasksFiles(taskId: Long, principal: Principal): List<TaskFileDto>?
    fun getFile(taskId: Long, fileId: Long, principal: Principal): ByteArray
    fun getMimeType(fileName: String): String
    fun getFileName(fileId: Long): String
    fun delete(fileId: Long): String
}