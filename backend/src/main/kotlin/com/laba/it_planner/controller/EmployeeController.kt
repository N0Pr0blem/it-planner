package com.laba.it_planner.controller

import com.laba.it_planner.dto.employee.EmployeeInviteDto
import com.laba.it_planner.dto.employee.EmployeeResponseDto
import com.laba.it_planner.dto.employee.EmployeeUpdateRoleDto
import com.laba.it_planner.mapper.EmployeeMapper
import com.laba.it_planner.service.EmployeeService
import com.laba.it_planner.service.ProjectService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1/project")
class EmployeeController(
    private val employeeService: EmployeeService,
    private val projectService: ProjectService,
    private val employeeMapper: EmployeeMapper
) {

    @GetMapping("/{projectId}/employee")
    @Operation(summary = "Find all employees of project if you are member of it")
    fun getAllEmployeesOfProject(
        @PathVariable(name = "projectId") projectId: Long,
        principal: Principal
    ): ResponseEntity<List<EmployeeResponseDto>> {
        val employees = employeeService.getAllProjectEmployee(projectId, principal)
        return ResponseEntity.ok().body(employeeMapper.toDtos(employees))
    }

    @PostMapping("/{projectId}/employee")
    @Operation(summary = "Invite employee")
    fun createEmployee(
        @PathVariable(name = "projectId") projectId: Long,
        @RequestBody employeeInviteDto: EmployeeInviteDto,
        principal: Principal
    ): ResponseEntity<EmployeeResponseDto> {
        val employee = projectService.inviteEmployee(projectId, employeeInviteDto, principal.name)
        return ResponseEntity.ok().body(employeeMapper.toDto(employee))
    }

    @DeleteMapping("/{projectId}/employee/{employeeId}")
    @Operation(summary = "Delete employee")
    fun deleteEmployee(
        @PathVariable(name = "projectId") projectId: Long,
        @PathVariable(name = "employeeId") employeeId: Long,
        principal: Principal
    ): ResponseEntity<String> {
        projectService.deleteEmployee(projectId, employeeId, principal.name)
        return ResponseEntity.ok().body("Employee was successfully deleted")
    }

    @PatchMapping("/{projectId}/employee/{employeeId}")
    @Operation(summary = "Change employee's project role")
    fun changeRole(
        @PathVariable(name = "projectId") projectId: Long,
        @PathVariable(name = "employeeId") employeeId: Long,
        @RequestBody employeeUpdateRoleDto: EmployeeUpdateRoleDto,
        principal: Principal
    ): ResponseEntity<EmployeeResponseDto> {
        val result = projectService.changeRole(projectId, employeeId,employeeUpdateRoleDto, principal.name)
        return ResponseEntity.ok().body(employeeMapper.toDto(result))
    }
}