package com.laba.it_planner.service.impl

import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.model.user.ProjectRole
import com.laba.it_planner.repository.EmployeeRepository
import com.laba.it_planner.service.EmployeeService
import com.laba.it_planner.service.OauthService
import com.laba.it_planner.service.UserInfoService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository,
    private val userInfoService: UserInfoService
) : EmployeeService {

    override fun createEmployee(employee: Employee): Employee {
        if (employeeRepository.findByProjectIdAndUserId(employee.project.id, employee.user.id).isPresent) {
            throw DataException("User already in project team", "ADD_EMPLOYEE_ERROR");
        } else return employeeRepository.save(employee)
    }

    override fun getAllProjectEmployee(projectId: Long, principal: Principal): List<Employee> {
        val user = userInfoService.getInfo(principal)
        val result = employeeRepository.findAllByProjectId(projectId)
        if (isContainUser(result, user)) {
            return result
        } else {
            throw DataException("You can't see not yours team", "MEMBER_EMPLOYEE_ERROR")
        }
    }

    override fun deleteEmployee(employeeId: Long) {
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId)
        }
    }

    override fun changeRole(employeeId: Long, newRole: ProjectRole): Employee {
        val foundedEmployee = employeeRepository.findById(employeeId)
        if (foundedEmployee.isPresent) {
            val employee = foundedEmployee.get()
            employee.projectRole = newRole
            return employeeRepository.save(employee)
        }
        throw DataException("No such employee exception", "EMPLOYEE_NOT_FOUND_ERROR")
    }

    override fun checkPermission(projectId: Long, principal: Principal): Boolean {
        return employeeRepository.existsByProjectIdAndUsername(projectId, principal.name)
    }

    override fun getByUserNameAndProjectId(
        name: String,
        projectId: Long
    ): Employee {
        val employeeOpt = employeeRepository.findByUsernameAndProjectId(projectId,name)
        if(employeeOpt.isPresent) {
            return employeeOpt.get()
        }
        else throw DataException("No such employee exception", "EMPLOYEE_NOT_FOUND_ERROR")
    }

    private fun isContainUser(employees: List<Employee>, user: OauthUser): Boolean {
        for (employee in employees) {
            if (employee.user == user) return true
        }
        return false
    }
}