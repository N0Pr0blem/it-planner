package com.laba.it_planner.service.impl

import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.repository.EmployeeRepository
import com.laba.it_planner.service.EmployeeService
import com.laba.it_planner.service.OauthService
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository,
    private val oauthService: OauthService
) : EmployeeService {

    override fun createEmployee(employee: Employee): Employee {
        if(employeeRepository.findByProjectIdAndUserId(employee.project.id,employee.user.id).isPresent){
            throw DataException("User already in project team","ADD_EMPLOYEE_ERROR");
        }
        else return employeeRepository.save(employee)
    }

    override fun getAllProjectEmployee(projectId: Long, username: String) :List<Employee>{
        val user = oauthService.getByUsername(username)
        val result = employeeRepository.findAllByProjectId(projectId)
        if(isContainUser(result,user)) {
            return result
        }
        else {
            throw DataException("You can't see not yours team", "MEMBER_EMPLOYEE_ERROR")
        }
    }

    private fun isContainUser(employees: List<Employee>, user: OauthUser): Boolean {
        for(employee in employees){
            if(employee.user == user)return true
        }
        return false
    }

}