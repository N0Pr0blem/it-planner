package com.laba.it_planner.controller.task

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.task.CreateTaskInfoRequestDto
import com.laba.it_planner.dto.task.TaskInfoListing
import com.laba.it_planner.dto.task.TaskInfoResponseDto
import com.laba.it_planner.dto.task.UpdateTaskInfoRequestDto
import com.laba.it_planner.mapper.task.TaskInfoMapper
import com.laba.it_planner.service.task.TaskService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1")
class TaskInfoController(
    private val taskService: TaskService,
    private val mapper: TaskInfoMapper
) {
    @Operation(summary = "Get all task of project")
    @GetMapping("/project/{projectId}/browse")
    fun getAll(
        @PathVariable(name = "projectId") projectId: Long,
        principal: Principal
    ): ResponseEntity<List<TaskInfoListing>> {
        return ResponseEntity.ok(taskService.getAll(projectId, principal))
    }

    @Operation(summary = "Get all my task from many projects")
    @GetMapping("/task/my")
    fun getMy(
        principal: Principal
    ): ResponseEntity<List<TaskInfoListing>> {
        return ResponseEntity.ok(taskService.getMy(principal))
    }

    @Operation(summary = "Create task for project")
    @PostMapping("/task")
    fun add(
        @RequestBody createTaskInfoRequestDto: CreateTaskInfoRequestDto,
        principal: Principal
    ): ResponseEntity<TaskInfoResponseDto> {
        val res = taskService.add(createTaskInfoRequestDto, principal)
        return ResponseEntity.ok(mapper.toDto(res))
    }

    @Operation(summary = "Get task by id")
    @GetMapping("/task/{taskId}")
    fun get(
        @PathVariable(name = "taskId") taskId: Long,
        principal: Principal
    ): ResponseEntity<TaskInfoResponseDto> {
        val res = taskService.get(taskId, principal)
        return ResponseEntity.ok(mapper.toDto(res))
    }

    @Operation(summary = "Get task description by id")
    @GetMapping("/task/{taskId}/description")
    fun getDescription(
        @PathVariable(name = "taskId") taskId: Long,
        principal: Principal
    ): ResponseEntity<MessageResponseDto> {
        val res = taskService.getDescription(taskId, principal)
        return ResponseEntity.ok(res)
    }

    @Operation(summary = "Update task")
    @PatchMapping("/task/{taskId}")
    fun patch(
        @PathVariable(name = "taskId") taskId: Long,
        @RequestBody updateTaskInfoRequestDto: UpdateTaskInfoRequestDto,
        principal: Principal
    ): ResponseEntity<TaskInfoResponseDto> {
        val res = taskService.update(taskId, updateTaskInfoRequestDto, principal)
        return ResponseEntity.ok(mapper.toDto(res))
    }

    @Operation(summary = "Delete task of project")
    @DeleteMapping("/task/{taskId}")
    fun delete(@PathVariable(name = "taskId") taskId: Long, principal: Principal): ResponseEntity<String> {
        taskService.delete(taskId, principal)
        return ResponseEntity.ok("Successfully deleted the task")
    }

    @Operation(summary = "Assign task to login user")
    @PutMapping("/project/{projectId}/task/{taskId}")
    fun assignToMe(
        @PathVariable(name = "taskId") taskId: Long,
        @PathVariable(name = "projectId") projectId: Long,
        principal: Principal
    ): ResponseEntity<String> {
        taskService.assignToMe(taskId, projectId, principal)
        return ResponseEntity.ok("Successfully assign the task")
    }

    @Operation(summary = "Assign task to someone employee")
    @PatchMapping("/project/{projectId}/task/{taskId}/employee/{employeeId}")
    fun assignToEmployee(
        @PathVariable(name = "taskId") taskId: Long,
        @PathVariable(name = "projectId") projectId: Long,
        @PathVariable(name = "employeeId") employeeId: Long,
        principal: Principal
    ): ResponseEntity<String> {
        taskService.assignToEmployee(taskId, projectId, employeeId, principal)
        return ResponseEntity.ok("Successfully assign the task")
    }
}