package com.laba.it_planner.controller

import com.laba.it_planner.dto.project.ProjectCreateRequestDto
import com.laba.it_planner.dto.project.ProjectCreateResponseDto
import com.laba.it_planner.mapper.ProjectCreationMapper
import com.laba.it_planner.service.ProjectService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/project")
class ProjectController(
    private val projectService: ProjectService,
    private val projectMapper: ProjectCreationMapper
) {
    @PostMapping()
    fun createProject(@RequestBody projectRequestDto: ProjectCreateRequestDto, principal: Principal): ResponseEntity<ProjectCreateResponseDto> {
        val response = projectService.createProject(projectRequestDto, principal.name)
        return ResponseEntity.ok(projectMapper.toDto(response))
    }
}