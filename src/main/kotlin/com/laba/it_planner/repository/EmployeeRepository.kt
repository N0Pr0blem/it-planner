package com.laba.it_planner.repository

import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.user.OauthUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface EmployeeRepository: JpaRepository<Employee, Long> {
    @Query("""
        select e from Employee e where e.project = :project and e.user=:user
        """)
    fun findByProjectIdAndByUserId(@Param("project") project: Project, @Param("user") user: OauthUser): Optional<Employee>
}