package com.laba.it_planner.service

import com.laba.it_planner.model.project.repository.ProjectRepo

interface ProjectRepoService {
    fun createProjectRepo(projectRepo: ProjectRepo): ProjectRepo
}