package com.laba.it_planner.controller.project

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.employee.EmployeeInviteDto
import com.laba.it_planner.dto.employee.EmployeeResponseDto
import com.laba.it_planner.dto.employee.EmployeeUpdateRoleDto
import com.laba.it_planner.mapper.project.EmployeeMapper
import com.laba.it_planner.service.project.EmployeeService
import com.laba.it_planner.service.project.ProjectService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/project")
class EmployeeController(
    private val employeeService: EmployeeService,
    private val projectService: ProjectService,
    private val employeeMapper: EmployeeMapper
) {
    private val logger: Logger = Logger.getLogger(EmployeeController::class.java.name)

    @GetMapping("/{projectId}/employee")
    @Operation(summary = "Find all employees of project if you are member of it")
    fun getAllEmployeesOfProject(
        @PathVariable(name = "projectId") projectId: Long,
        principal: Principal
    ): ResponseEntity<List<EmployeeResponseDto>> {
        logger.info("EVENT_GET_ALL_EMPLOYEES | Start getting all employees")
        val employees = employeeService.getAllProjectEmployee(projectId, principal)
        logger.info("EVENT_GET_ALL_EMPLOYEES | End getting all employees")
        return ResponseEntity.ok().body(employeeMapper.toDtos(employees))
    }

    @GetMapping("/{projectId}/employee/{employeeId}")
    @Operation(summary = "Find all employees of project if you are member of it")
    fun getAllEmployeeOfProject(
        @PathVariable(name = "projectId") projectId: Long,
        @PathVariable(name = "employeeId") employeeId: Long,
        principal: Principal
    ): ResponseEntity<EmployeeResponseDto> {
        logger.info("EVENT_GET_ALL_EMPLOYEES | Start getting all employees if you member of it")
        val employee = employeeService.getEmployeeInfo(projectId, employeeId, principal)
        logger.info("EVENT_GET_ALL_EMPLOYEES | End getting all employees if you member of it")
        return ResponseEntity.ok().body(employeeMapper.toDto(employee))
    }

    @PostMapping("/{projectId}/employee")
    @Operation(summary = "Invite employee")
    fun createEmployee(
        @PathVariable(name = "projectId") projectId: Long,
        @RequestBody employeeInviteDto: EmployeeInviteDto,
        principal: Principal
    ): ResponseEntity<EmployeeResponseDto> {
        logger.info("EVENT_CREATE_EMPLOYEE | Start creating employee")
        val employee = projectService.inviteEmployee(projectId, employeeInviteDto, principal.name)
        logger.info("EVENT_CREATE_EMPLOYEE | End creating employee")
        return ResponseEntity.ok().body(employeeMapper.toDto(employee))
    }

    @DeleteMapping("/{projectId}/employee/{employeeId}")
    @Operation(summary = "Delete employee")
    fun deleteEmployee(
        @PathVariable(name = "projectId") projectId: Long,
        @PathVariable(name = "employeeId") employeeId: Long,
        principal: Principal
    ): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_DELETE_EMPLOYEE | Start deleting employee")
        projectService.deleteEmployee(projectId, employeeId, principal.name)
        logger.info("EVENT_DELETE_EMPLOYEE | End deleting employee")
        return ResponseEntity.ok().body(MessageResponseDto("Employee was successfully deleted"))
    }

    @PatchMapping("/{projectId}/employee/{employeeId}")
    @Operation(summary = "Change employee's project role")
    fun changeRole(
        @PathVariable(name = "projectId") projectId: Long,
        @PathVariable(name = "employeeId") employeeId: Long,
        @RequestBody employeeUpdateRoleDto: EmployeeUpdateRoleDto,
        principal: Principal
    ): ResponseEntity<EmployeeResponseDto> {
        logger.info("EVENT_CHANGE_ROLE | Start changing role")
        val result = projectService.changeRole(projectId, employeeId, employeeUpdateRoleDto, principal.name)
        logger.info("EVENT_CHANGE_ROLE | End changing role")
        return ResponseEntity.ok().body(employeeMapper.toDto(result))
    }
}