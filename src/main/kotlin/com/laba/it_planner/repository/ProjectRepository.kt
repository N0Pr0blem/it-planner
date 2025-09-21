package com.laba.it_planner.repository

import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.user.OauthUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProjectRepository: JpaRepository<Project, Long> {
    @Query(value = """
        SELECT p FROM Project p WHERE name=:name AND created_user=:user;
    """, nativeQuery = true)
    fun findByNameAndByCreatedUser(@Param("name") name: String, @Param("user") user: OauthUser): Optional<Project>
}