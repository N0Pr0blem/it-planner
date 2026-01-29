package com.laba.it_planner.service

import org.springframework.web.multipart.MultipartFile
import java.security.Principal

interface FileService {
    fun saveFile(path: String, file: MultipartFile): String
    fun getFile(path: String): ByteArray
    fun deleteFile(path: String)
    fun createDescriptionFileForTask(projectName: String, principal: Principal, taskUUID: String, description: String): String
    fun updateFile(descriptionFile: String?, description: String?):String
    fun getCustomFilename(originalFilename: String?): String
}