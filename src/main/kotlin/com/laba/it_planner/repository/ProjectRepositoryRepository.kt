package com.laba.it_planner.repository

import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepositoryRepository: JpaRepository<ProjectRepository, Long> {
}