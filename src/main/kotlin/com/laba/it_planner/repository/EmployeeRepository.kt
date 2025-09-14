package com.laba.it_planner.repository

import com.laba.it_planner.model.project.Employee
import org.springframework.data.jpa.repository.JpaRepository

interface EmployeeRepository: JpaRepository<Employee, Long> {
}