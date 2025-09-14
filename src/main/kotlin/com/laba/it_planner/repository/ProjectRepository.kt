package com.laba.it_planner.repository

import com.laba.it_planner.model.project.Project
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository: JpaRepository<Project, Long> {
}