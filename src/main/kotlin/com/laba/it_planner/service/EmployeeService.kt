package com.laba.it_planner.service

import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.user.ProjectRole

interface EmployeeService {
    fun createEmployee(employee: Employee): Employee
    fun getAllProjectEmployee(projectId: Long, username: String):List<Employee>
    fun deleteEmployee(employeeId: Long)
    fun changeRole(employeeId: Long,newRole: ProjectRole): Employee
}