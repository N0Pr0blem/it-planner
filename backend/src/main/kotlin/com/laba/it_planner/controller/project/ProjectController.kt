package com.laba.it_planner.controller.project

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.project.ProjectCreateRequestDto
import com.laba.it_planner.dto.project.ProjectCreateResponseDto
import com.laba.it_planner.dto.project.ProjectListingDto
import com.laba.it_planner.mapper.project.ProjectCreationMapper
import com.laba.it_planner.mapper.project.ProjectListingMapper
import com.laba.it_planner.service.project.ProjectService
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
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/project")
class ProjectController(
    private val projectService: ProjectService,
    private val projectMapper: ProjectCreationMapper,
    private val projectListingMapper: ProjectListingMapper
) {
    private val logger: Logger = Logger.getLogger(ProjectController::class.java.name)

    @PostMapping()
    @Operation(summary = "Create new project")
    fun createProject(@Valid @RequestBody projectRequestDto: ProjectCreateRequestDto, principal: Principal): ResponseEntity<ProjectCreateResponseDto> {
        logger.info("EVENT_CREATE_PROJECT | Start creating project")
        val response = projectService.createProject(projectRequestDto, principal)
        logger.info("EVENT_CREATE_PROJECT | End creating project")
        return ResponseEntity.ok(projectMapper.toDto(response))
    }

    @GetMapping()
    @Operation(summary = "Get all projects of the user")
    fun getProjects(principal: Principal): ResponseEntity<List<ProjectListingDto>> {
        logger.info("EVENT_GET_ALL_PROJECTS | Start getting projects")
        val response = projectService.getAllProjects(principal.name)
        logger.info("EVENT_GET_ALL_PROJECTS | End getting projects")
        return ResponseEntity.ok(projectListingMapper.toDtos(response))
    }

    @GetMapping("/my")
    @Operation(summary = "Get all projects of the user")
    fun getMyProjects(principal: Principal): ResponseEntity<List<ProjectListingDto>> {
        logger.info("EVENT_GET_ALL_USERS_PROJECTS | Start getting user's projects")
        val response = projectService.getAllUsersProjects(principal.name)
        logger.info("EVENT_GET_ALL_USERS_PROJECTS | End getting user's projects")
        return ResponseEntity.ok(projectListingMapper.toDtos(response))
    }

    @DeleteMapping("{projectId}")
    @Operation(summary = "Delete project")
    fun deleteProject(@PathVariable projectId: Long, principal: Principal): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_DELETE_PROJECT | Start deleting project")
        projectService.deleteProject(projectId, principal)
        logger.info("EVENT_DELETE_PROJECT | End deleting project")
        return ResponseEntity.ok(MessageResponseDto(message = "Project successfully delete"))
    }
}
