package com.laba.it_planner.repository

import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.user.OauthUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {
    @Query(
        """
    SELECT * FROM project p
    WHERE p.name = :name AND p.created_user = :userId
""", nativeQuery = true
    )
    fun findByNameAndByCreatedUser(
        @Param("name") name: String,
        @Param("userId") userId: Long?
    ): Optional<Project>

    fun findAllByCreatedUser(user: OauthUser): List<Project>

    @Query(
        """
    SELECT p.*
FROM user_info ui
         left join employee e ON e.user_id = ui.id
         left join project p ON e.project_id = p.id
where ui.email = :username
""", nativeQuery = true
    )
    fun getAllUsersProjectsByUsername(@Param("username") username: String): List<Project>

    @Query(
        """
    SELECT p.* from task_info ti 
    left join project p ON p.id = ti.project_id
    where ti.id = :task_id
""", nativeQuery = true
    )
    fun findByTaskId(@Param("task_id") taskId: Long): Project
}