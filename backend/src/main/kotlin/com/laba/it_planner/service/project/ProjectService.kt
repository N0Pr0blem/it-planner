package com.laba.it_planner.service.project

import com.laba.it_planner.dto.employee.EmployeeInviteDto
import com.laba.it_planner.dto.employee.EmployeeUpdateRoleDto
import com.laba.it_planner.dto.project.ProjectCreateRequestDto
import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.project.Project
import java.security.Principal

interface ProjectService {
    fun createProject(projectCreateRequestDto: ProjectCreateRequestDto, principal: Principal): Project

    fun getAllUsersProjects(username: String): List<Project>

    fun inviteEmployee(projectId: Long, employeeInviteDto: EmployeeInviteDto, username: String): Employee

    fun deleteEmployee(projectId: Long, employeeId: Long, username: String)

    fun changeRole(
        projectId: Long,
        employeeId: Long,
        employeeUpdateRoleDto: EmployeeUpdateRoleDto,
        name: String
    ): Employee

    fun get(projectId: Long): Project
    fun getAllProjects(name: String) :List<Project>
    fun deleteProject(projectId: Long, principal: Principal)
    fun getByTaskId(taskId: Long): Project
}