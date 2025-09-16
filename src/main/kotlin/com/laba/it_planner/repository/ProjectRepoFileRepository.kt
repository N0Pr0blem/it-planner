package com.laba.it_planner.repository;

import com.laba.it_planner.model.project.repository.ProjectRepoFile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepoFileRepository: JpaRepository<ProjectRepoFile, Long> {
}
