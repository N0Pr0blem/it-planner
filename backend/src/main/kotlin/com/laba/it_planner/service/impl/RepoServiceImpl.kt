package com.laba.it_planner.service.impl

import com.laba.it_planner.exception.AccessException
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.project.repository.FileType
import com.laba.it_planner.model.project.repository.ProjectRepoFile
import com.laba.it_planner.repository.ProjectRepoFileRepository
import com.laba.it_planner.repository.ProjectRepoRepository
import com.laba.it_planner.service.FileService
import com.laba.it_planner.service.RepoService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@Service
class RepoServiceImpl(
    private val repoRepository: ProjectRepoRepository,
    private val fileRepo: ProjectRepoFileRepository,
    private val fileService: FileService,
) : RepoService {
    override fun save(
        projectId: Long,
        file: MultipartFile,
        fileType: FileType,
        principal: Principal
    ): String {
        val projectRepoOpt = repoRepository.findByProjectIdAndUsername(projectId, principal.name)
        if (projectRepoOpt.isPresent) {
            val projectRepo = projectRepoOpt.get()
            val path = projectRepo.path + fileType.prefix + file.originalFilename

            if (!fileRepo.findByNameAndProjectRepoId(file.originalFilename,projectRepo.id).isPresent) {
                fileRepo.save(
                    ProjectRepoFile(
                        name = file.originalFilename,
                        type = fileType,
                        projectRepo = projectRepo
                    )
                )
                return fileService.saveFile(path, file)
            }
            else throw DataException("File already exists","FILE_EXIST_ERROR")


        } else throw AccessException("You don't have permission to this repository", "PERMISSION_DENIED")
    }

    override fun getAllRepositoryFiles(
        projectId: Long,
        principal: Principal
    ): List<ProjectRepoFile> {
        val projectRepoOpt = repoRepository.findByProjectIdAndUsername(projectId, principal.name)
        if (projectRepoOpt.isPresent) {
            val projectRepo = projectRepoOpt.get()
            val result = fileRepo.findAllByProjectRepo(projectRepo)

            return result
        } else throw AccessException("You don't have permission to this repository", "PERMISSION_DENIED")
    }

    override fun getFile(projectId: Long, fileId: Long, principal: Principal): ByteArray {
        val projectRepoOpt = repoRepository.findByProjectIdAndUsername(projectId, principal.name)
        val file = getAllRepositoryFiles(projectId, principal)
            .stream()
            .filter { repoFile -> repoFile.id == fileId }
            .findFirst()
            .orElseThrow {
                DataException("No such file in repository", "NO_SUCH_FILE")
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
}