package com.laba.it_planner.repository;

import com.laba.it_planner.model.project.repository.ProjectRepositoryFile
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepositoryFileRepository: JpaRepository<ProjectRepositoryFile, Long> {
}
