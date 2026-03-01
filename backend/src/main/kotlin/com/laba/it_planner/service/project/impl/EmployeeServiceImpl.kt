package com.laba.it_planner.service.project.impl

import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.model.user.ProjectRole
import com.laba.it_planner.repository.project.EmployeeRepository
import com.laba.it_planner.service.project.EmployeeService
import com.laba.it_planner.service.storage.FileService
import com.laba.it_planner.service.user.UserInfoService
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.Principal
import java.util.*

@Service
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository,
    private val userInfoService: UserInfoService,
    private val fileService: FileService
) : EmployeeService {

    override fun createEmployee(employee: Employee): Employee {
        if (employeeRepository.findByProjectIdAndUserId(employee.project.id, employee.user.id).isPresent) {
            throw DataException("error.employee.exist", employee.project.id.toString());
        } else return employeeRepository.save(employee)
    }

    override fun getAllProjectEmployee(projectId: Long, principal: Principal): List<Employee> {
        val user = userInfoService.getUserInfo(principal)
        val result = employeeRepository.findAllByProjectId(projectId)
        if (isContainUser(result, user)) {
            result.forEach { e -> e.user.profileImage = setImage(e.user.id!!, e.user.profileImage) }
            return result
        } else {
            throw DataException("error.employee.access", "")
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
        throw DataException("error.employee.not_exist", employeeId.toString())
    }

    override fun checkPermission(projectId: Long, principal: Principal): Boolean {
        return employeeRepository.existsByProjectIdAndUsername(projectId, principal.name)
    }

    override fun getByUserNameAndProjectId(
        name: String,
        projectId: Long
    ): Employee {
        val employeeOpt = employeeRepository.findByUsernameAndProjectId(projectId, name)
        if (employeeOpt.isPresent) {
            return employeeOpt.get()
        } else throw DataException("error.employee.not_exist.in_project", listOf(name, projectId.toString()))
    }

    override fun getEmployeeInfo(projectId: Long, employeeId: Long, principal: Principal): Employee {
        val user = userInfoService.getUserInfo(principal)
        val result = employeeRepository.findAllByProjectId(projectId)
        if (isContainUser(result, user) && isContainEmployee(result, employeeId)) {
            val employee = result.stream().filter { employee -> employee.id == employeeId }.findFirst().get()
            employee.user.profileImage = setImage(employee.user.id!!, employee.user.profileImage)
            return employee
        } else {
            throw DataException("error.employee.access", "")
        }
    }

    override fun getById(employeeId: Long): Employee {
        return employeeRepository.findById(employeeId)
            .orElseThrow { DataException("error.employee.not_exist", employeeId.toString()) }
    }

    private fun isContainUser(employees: List<Employee>, user: OauthUser): Boolean {
        for (employee in employees) {
            if (employee.user == user) return true
        }
        return false
    }

    private fun isContainEmployee(employees: List<Employee>, employeeId: Long): Boolean {
        for (employee in employees) {
            if (employee.id == employeeId) return true
        }
        return false
    }

    fun setImage(id: Long, path: String?): String {
        if (path != null) {
            val image = fileService.getFile("users/user_${id}/profile/${path}")
            val encoded: ByteArray = Base64.getEncoder().encode(image)
            return String(encoded, StandardCharsets.UTF_8)
        } else return ""
    }

}