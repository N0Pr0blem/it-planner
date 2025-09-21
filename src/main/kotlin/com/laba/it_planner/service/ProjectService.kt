package com.laba.it_planner.service

import com.laba.it_planner.dto.project.ProjectCreateRequestDto
import com.laba.it_planner.model.project.Project

interface ProjectService {
    fun createProject(projectCreateRequestDto: ProjectCreateRequestDto, username: String): Project
}