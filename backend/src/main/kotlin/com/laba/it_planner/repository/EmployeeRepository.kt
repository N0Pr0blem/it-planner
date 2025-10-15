package com.laba.it_planner.repository

import com.laba.it_planner.model.project.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EmployeeRepository: JpaRepository<Employee, Long> {
    @Query("""
    select * from employee e 
    where e.project_id = :projectId and e.user_id = :userId
""", nativeQuery = true)
    fun findByProjectIdAndUserId(
        @Param("projectId") projectId: Long?,
        @Param("userId") userId: Long?
    ): Optional<Employee>

    fun findAllByProjectId(projectId: Long):List<Employee>
}