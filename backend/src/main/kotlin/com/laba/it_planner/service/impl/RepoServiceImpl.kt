package com.laba.it_planner.service.impl

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.repo.ProjectRepoFileDto
import com.laba.it_planner.exception.AccessException
import com.laba.it_planner.exception.ApiException
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.mapper.ProjectRepoMapper
import com.laba.it_planner.model.project.repository.FileType
import com.laba.it_planner.model.project.repository.ProjectRepoFile
import com.laba.it_planner.repository.ProjectRepoFileRepository
import com.laba.it_planner.repository.ProjectRepoRepository
import com.laba.it_planner.service.FileService
import com.laba.it_planner.service.RepoService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.util.stream.Collectors

@Service
class RepoServiceImpl(
    private val repoRepository: ProjectRepoRepository,
    private val fileRepo: ProjectRepoFileRepository,
    private val fileService: FileService,
    private val projectRepoMapper: ProjectRepoMapper
) : RepoService {
    override fun save(
        projectId: Long,
        file: MultipartFile,
        fileType: FileType,
        principal: Principal
    ): MessageResponseDto {
        val projectRepoOpt = repoRepository.findByProjectIdAndUsername(projectId, principal.name)
        if (projectRepoOpt.isPresent) {
            val projectRepo = projectRepoOpt.get()
            val path = projectRepo.path + fileType.prefix + file.originalFilename

            if (!fileRepo.findByNameAndProjectRepoId(file.originalFilename, projectRepo.id).isPresent) {
                val repoFile = fileRepo.save(
                    ProjectRepoFile(
                        name = file.originalFilename,
                        type = fileType,
                        projectRepo = projectRepo
                    )
                )
                var result = ""

                try {
                    result = fileService.saveFile(path, file)
                } catch (e: ApiException) {
                    result = e.message ?: ""
                    fileRepo.delete(repoFile)
                }

                return MessageResponseDto(message = result)

            } else throw DataException("file.originalFilename", file.originalFilename.toString())


        } else throw AccessException("error.repo.file.permission", "")
    }

    override fun getAllRepositoryFiles(
        projectId: Long,
        principal: Principal
    ): List<ProjectRepoFileDto> {
        val projectRepoOpt = repoRepository.findByProjectIdAndUsername(projectId, principal.name)
        if (projectRepoOpt.isPresent) {
            val projectRepo = projectRepoOpt.get()
            val result = fileRepo.findAllByProjectRepo(projectRepo)

            return result.stream().map(projectRepoMapper::toDto).collect(Collectors.toList())
        } else throw AccessException("error.repo.file.permission", "")
    }

    override fun getFile(projectId: Long, fileId: Long, principal: Principal): ByteArray {
        val projectRepoOpt = repoRepository.findByProjectIdAndUsername(projectId, principal.name)
        val file = getAllRepositoryFiles(projectId, principal)
            .stream()
            .filter { repoFile -> repoFile.id == fileId }
            .findFirst()
            .orElseThrow {
                DataException("error.repo.file.not_exist", fileId.toString())
            }

        val path = projectRepoOpt.get().path + file.type.prefix + file.name

        return fileService.getFile(path)
    }

    private fun getFileExtension(filename: String?): String {
        return if (filename?.contains(".") == true) {
            filename.substringAfterLast(".").lowercase()
        } else {
            "bin"
        }
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
        return fileRepo.findById(fileId).get().name ?: ""
    }

    override fun delete(projectId: Long, fileId: Long, principal: Principal): String {
        var res = "File doesn't exist or you don't have permission to this project"
        val projectRepoOpt = repoRepository.findByProjectIdAndUsername(projectId, principal.name)
        if(projectRepoOpt.isPresent) {
            fileRepo.deleteById(fileId)
            res = "Successfully deleted file"
        }
        return res
    }
}