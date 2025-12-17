package com.laba.it_planner.controller

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.project.ProjectCreateRequestDto
import com.laba.it_planner.dto.project.ProjectCreateResponseDto
import com.laba.it_planner.dto.project.ProjectListingDto
import com.laba.it_planner.mapper.ProjectCreationMapper
import com.laba.it_planner.mapper.ProjectListingMapper
import com.laba.it_planner.model.project.Project
import com.laba.it_planner.service.ProjectService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/project")
class ProjectController(
    private val projectService: ProjectService,
    private val projectMapper: ProjectCreationMapper,
    private val projectListingMapper: ProjectListingMapper
) {
    @PostMapping()
    @Operation(summary = "Create new project")
    fun createProject(@Valid @RequestBody projectRequestDto: ProjectCreateRequestDto, principal: Principal): ResponseEntity<ProjectCreateResponseDto> {
        val response = projectService.createProject(projectRequestDto, principal)
        return ResponseEntity.ok(projectMapper.toDto(response))
    }

    @GetMapping()
    @Operation(summary = "Get all projects of the user")
    fun getProjects(principal: Principal): ResponseEntity<List<ProjectListingDto>> {
        val response = projectService.getAllProjects(principal.name)
        return ResponseEntity.ok(projectListingMapper.toDtos(response))
    }

    @GetMapping("/my")
    @Operation(summary = "Get all projects of the user")
    fun getMyProjects(principal: Principal): ResponseEntity<List<ProjectListingDto>> {
        val response = projectService.getAllUsersProjects(principal.name)
        return ResponseEntity.ok(projectListingMapper.toDtos(response))
    }

    @DeleteMapping("{projectId}")
    @Operation(summary = "Delete project")
    fun deleteProject(@PathVariable projectId: Long, principal: Principal): ResponseEntity<MessageResponseDto> {
        projectService.deleteProject(projectId, principal)
        return ResponseEntity.ok(MessageResponseDto(message = "Project successfully delete"))
    }
}
