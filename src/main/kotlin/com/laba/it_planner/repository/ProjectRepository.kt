package com.laba.it_planner.repository

import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.user.OauthUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectRepository: JpaRepository<Project, Long> {
    @Query("""
    SELECT * FROM project p
    WHERE p.name = :name AND p.created_user = :userId
""", nativeQuery = true)
    fun findByNameAndByCreatedUser(
        @Param("name") name: String,
        @Param("userId") userId: Long?
    ): Optional<Project>

    fun findAllByCreatedUser(user: OauthUser): List<Project>
}