package com.laba.it_planner.service.task

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.task.TaskFileDto
import com.laba.it_planner.mapper.task.TaskFileMapper
import com.laba.it_planner.repository.task.TaskFileRepository
import com.laba.it_planner.service.FileService
import com.laba.it_planner.service.TaskFileService
import com.laba.it_planner.service.TaskInfoService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Service
class TaskFileServiceImpl(
    private val taskInfoService: TaskInfoService,
    private val fileService: FileService,
    private val taskFileRepository: TaskFileRepository,
    private val taskFileMapper: TaskFileMapper,
) : TaskFileService {
    override fun save(
        file: MultipartFile,
        taskId: Long,
        principal: Principal
    ): MessageResponseDto? {
        val taskInfo = taskInfoService.get(taskId, principal)
        val path = taskInfoService.getPathForTaskFolder(taskId)
        if (path != null) {
            val res = fileService.saveFile(path + file.originalFilename, file)

            taskFileRepository.save(TaskFile(null, taskInfo, FileType.TASK, file.originalFilename))

            return MessageResponseDto(res)
        } else return MessageResponseDto("Error to save file")
    }

    override fun getAllTasksFiles(
        taskId: Long,
        principal: Principal
    ): List<TaskFileDto> {
        val taskFiles = taskFileRepository.findAllByTaskId(taskId)

        return taskFiles
            ?.mapNotNull { tf ->
                try {
                    taskFileMapper.toDto(tf)
                } catch (e: NullPointerException) {
                    print("Failed to map task file: $tf " + e.message)
                    null
                }
            }
            ?: emptyList()
    }

    override fun getFile(
        taskId: Long,
        fileId: Long,
        principal: Principal
    ): ByteArray {
        val files = getAllTasksFiles(taskId, principal)
        if (files != null) {
            val file = files.stream().filter { file -> file.id == fileId }.findFirst()
            val path = taskInfoService.getPathForTaskFolder(taskId) + file.get().name

            return fileService.getFile(path)
        } else return ByteArray(0)
    }

    override fun getMimeType(fileName: String): String {
        val extension = fileName.substringAfterLast('.').lowercase()
        return when (extension) {
            "pdf" -> "application/pdf"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "odt" -> "application/vnd.oasis.opendocument.text"
            "xls" -> "application/vnd.ms-excel"
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "ppt" -> "application/vnd.ms-powerpoint"
            "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "zip" -> "application/zip"
            "txt" -> "text/plain"
            "html" -> "text/html"
            "css" -> "text/css"
            "js" -> "application/javascript"
            "json" -> "application/json"
            "xml" -> "application/xml"
            else -> "application/octet-stream"
        }
    }

    override fun getFileName(fileId: Long): String {
        return taskFileRepository.findById(fileId).get().name!!
    }

    override fun delete(fileId: Long): String {
        if (taskFileRepository.existsById(fileId)) {
            taskFileRepository.deleteById(fileId)
        }
        return "Successfully deleted file"
    }
}