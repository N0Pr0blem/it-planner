package com.laba.it_planner.repository

import com.laba.it_planner.model.project.Employee
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {
    @Query("""
    select * from employee e 
    where e.project_id = :projectId and e.user_id = :userId
    """, nativeQuery = true
    )
    fun findByProjectIdAndUserId(
        @Param("projectId") projectId: Long?,
        @Param("userId") userId: Long?
    ): Optional<Employee>

    fun findAllByProjectId(projectId: Long): List<Employee>

    @Query("""
        WITH user_info as (SELECT u.id
              FROM oauth_user u
              WHERE u.username = :username)
SELECT EXISTS(SELECT 1
              FROM employee e
                       JOIN user_info u ON u.id = e.user_id
              WHERE e.project_id = :projectId)
    """, nativeQuery = true)
    fun existsByProjectIdAndUsername(@Param("projectId") projectId: Long,
                                     @Param("username") username: String): Boolean


    @Query("""
    select * from employee e 
    join user_info u ON e.user_id = u.id
    where e.project_id = :projectId and u.email = :username
    """, nativeQuery = true
    )
    @EntityGraph(attributePaths = ["user"])
    fun findByUsernameAndProjectId(@Param("projectId") projectId: Long?,
                                   @Param("username") username: String?): Optional<Employee>
}