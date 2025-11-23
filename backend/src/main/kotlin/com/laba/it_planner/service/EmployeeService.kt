package com.laba.it_planner.service

import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.user.ProjectRole
import java.security.Principal

interface EmployeeService {
    fun createEmployee(employee: Employee): Employee
    fun getAllProjectEmployee(projectId: Long, principal: Principal):List<Employee>
    fun deleteEmployee(employeeId: Long)
    fun changeRole(employeeId: Long,newRole: ProjectRole): Employee
    fun checkPermission(projectId: Long, principal: Principal): Boolean
    fun getByUserNameAndProjectId(name: String, projectId: Long): Employee
}