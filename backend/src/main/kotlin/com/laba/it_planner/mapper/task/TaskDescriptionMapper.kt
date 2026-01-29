package com.laba.it_planner.mapper.task

import com.laba.it_planner.service.storage.FileService
import org.springframework.stereotype.Component

@Component
class TaskDescriptionMapper(
    private val fileService: FileService
) {
    fun toDto(descriptionPath: String?): String {
        if (descriptionPath != null && !descriptionPath.isEmpty()) {
            val byteArray = fileService.getFile(descriptionPath)
            return String(byteArray, Charsets.UTF_8)
        } else return ""
    }
}