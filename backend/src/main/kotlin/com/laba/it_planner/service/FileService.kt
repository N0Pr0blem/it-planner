package com.laba.it_planner.service

import org.springframework.web.multipart.MultipartFile

interface FileService {
    fun saveFile(path: String, file: MultipartFile): String
    fun getFile(path: String): ByteArray
    fun deleteFile(path: String)
}