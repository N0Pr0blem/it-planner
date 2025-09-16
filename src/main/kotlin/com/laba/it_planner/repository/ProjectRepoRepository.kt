package com.laba.it_planner.repository

import com.laba.it_planner.model.project.repository.ProjectRepo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepoRepository: JpaRepository<ProjectRepo, Long> {
}