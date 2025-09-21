package com.laba.it_planner.service

import com.laba.it_planner.model.project.Employee

interface EmployeeService {
    fun createEmployee(employee: Employee): Employee
}