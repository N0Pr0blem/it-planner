package com.laba.it_planner.controller

import com.laba.it_planner.dto.employee.EmployeeResponseDto
import com.laba.it_planner.mapper.EmployeeMapper
import com.laba.it_planner.service.EmployeeService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/project")
class EmployeeController(
    private val employeeService: EmployeeService,
    private val employeeMapper: EmployeeMapper
) {

    @GetMapping("/{projectId}/employees")
    @Operation(summary = "Find all employees of project if you are member of it")
    fun getAllEmployeesOfProject(
        @PathVariable(name = "projectId") projectId: Long,
        principal: Principal
    ): ResponseEntity<List<EmployeeResponseDto>> {
        val employees = employeeService.getAllProjectEmployee(projectId, principal.name)
        return ResponseEntity.ok().body(employeeMapper.toDtos(employees))
    }
}