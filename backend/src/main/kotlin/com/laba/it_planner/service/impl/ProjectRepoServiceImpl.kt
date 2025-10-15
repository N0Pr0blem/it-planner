package com.laba.it_planner.service.impl

import com.laba.it_planner.model.project.repository.ProjectRepo
import com.laba.it_planner.repository.ProjectRepoRepository
import com.laba.it_planner.service.ProjectRepoService
import org.springframework.stereotype.Service

@Service
class ProjectRepoServiceImpl(
    private val projectRepoRepository: ProjectRepoRepository
) : ProjectRepoService {

    override fun createProjectRepo(projectRepo: ProjectRepo): ProjectRepo {
        return projectRepoRepository.save(projectRepo)
    }

}