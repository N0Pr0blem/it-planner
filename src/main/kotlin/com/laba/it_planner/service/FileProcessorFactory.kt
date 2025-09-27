package com.laba.it_planner.service

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class FileProcessorFactory(
    private val processors: List<FileProcessor>
) {

    fun getProcessor(file: MultipartFile): FileProcessor {
        return processors.find { it.supports(file) }
            ?: throw IllegalArgumentException("Unsupported file type: ${file.originalFilename}")
    }

    fun getProcessorByExtension(extension: String): FileProcessor? {
        return processors.find { it.getSupportedExtensions().contains(extension.lowercase()) }
    }
}