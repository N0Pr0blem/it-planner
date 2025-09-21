package com.laba.it_planner.service.impl

import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.repository.EmployeeRepository
import com.laba.it_planner.service.EmployeeService
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository
) : EmployeeService {

    override fun createEmployee(employee: Employee): Employee {
        if(employeeRepository.findByProjectIdAndByUserId(employee.project,employee.user).isPresent){
            throw DataException("User already in project team","ADD_EMPLOYEE_ERROR");
        }
        else return employeeRepository.save(employee)
    }

}