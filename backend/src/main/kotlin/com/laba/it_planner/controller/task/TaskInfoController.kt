package com.laba.it_planner.controller.task

import com.laba.it_planner.controller.storage.StorageController
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
import java.util.Locale
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1")
class TaskInfoController(
    private val taskService: TaskService,
    private val mapper: TaskInfoMapper
) {
    private val logger: Logger = Logger.getLogger(TaskInfoController::class.java.name)

    @Operation(summary = "Get all task of project")
    @GetMapping("/project/{projectId}/browse")
    fun getAll(
        @PathVariable(name = "projectId") projectId: Long,
        principal: Principal
    ): ResponseEntity<List<TaskInfoListing>> {
        logger.info("EVENT_GET_ALL_TASKS | Start getting all tasks")
        val res = taskService.getAll(projectId, principal)
        logger.info("EVENT_GET_ALL_TASKS | Ending getting all tasks")
        return ResponseEntity.ok(res)
    }

    @Operation(summary = "Get all my task from many projects")
    @GetMapping("/task/my")
    fun getMy(
        principal: Principal
    ): ResponseEntity<List<TaskInfoListing>> {
        logger.info("EVENT_GET_ALL_TASKS | Start getting all tasks")
        val res = taskService.getMy(principal)
        logger.info("EVENT_GET_ALL_TASKS | Ending getting all tasks")
        return ResponseEntity.ok(res)
    }

    @Operation(summary = "Create task for project")
    @PostMapping("/task")
    fun add(
        @RequestBody createTaskInfoRequestDto: CreateTaskInfoRequestDto,
        principal: Principal
    ): ResponseEntity<TaskInfoResponseDto> {
        logger.info("EVENT_CREATE_TASK_INFO | Start creating new task")
        val res = taskService.add(createTaskInfoRequestDto, principal)
        logger.info("EVENT_CREATE_TASK_INFO | Ending creating new task")
        return ResponseEntity.ok(mapper.toDto(res))
    }

    @Operation(summary = "Get task by id")
    @GetMapping("/task/{taskId}")
    fun get(
        @PathVariable(name = "taskId") taskId: Long,
        principal: Principal
    ): ResponseEntity<TaskInfoResponseDto> {
        logger.info("EVENT_GET_TASKS | Start getting task by id")
        val res = taskService.get(taskId, principal)
        logger.info("EVENT_GET_TASKS | Ending getting task by id")
        return ResponseEntity.ok(mapper.toDto(res))
    }

    @Operation(summary = "Get task description by id")
    @GetMapping("/task/{taskId}/description")
    fun getDescription(
        @PathVariable(name = "taskId") taskId: Long,
        principal: Principal
    ): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_GET_TASKS | Start getting task description")
        val res = taskService.getDescription(taskId, principal)
        logger.info("EVENT_GET_TASKS | Ending getting task description")
        return ResponseEntity.ok(res)
    }

    @Operation(summary = "Update task")
    @PatchMapping("/task/{taskId}")
    fun patch(
        @PathVariable(name = "taskId") taskId: Long,
        @RequestBody updateTaskInfoRequestDto: UpdateTaskInfoRequestDto,
        principal: Principal
    ): ResponseEntity<TaskInfoResponseDto> {
        logger.info("EVENT_UPDATE_TASK_INFO | Start updating task")
        val res = taskService.update(taskId, updateTaskInfoRequestDto, principal)
        logger.info("EVENT_UPDATE_TASK_INFO | Ending updating task")
        return ResponseEntity.ok(mapper.toDto(res))
    }

    @Operation(summary = "Delete task of project")
    @DeleteMapping("/task/{taskId}")
    fun delete(@PathVariable(name = "taskId") taskId: Long, principal: Principal): ResponseEntity<String> {
        logger.info("EVENT_DELETE_PROJECT | Start deleting project")
        taskService.delete(taskId, principal)
        logger.info("EVENT_DELETE_PROJECT | Ending deleting project")
        return ResponseEntity.ok("Successfully deleted the task")
    }

    @Operation(summary = "Assign task to login user")
    @PutMapping("/project/{projectId}/task/{taskId}")
    fun assignToMe(
        @PathVariable(name = "taskId") taskId: Long,
        @PathVariable(name = "projectId") projectId: Long,
        principal: Principal,
        locale: Locale
    ): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_ASSIGN_TASK_INFO | Start assigning project to login user")
        val res =MessageResponseDto(taskService.assignToMe(taskId, projectId, principal,locale))
        logger.info("EVENT_ASSIGN_TASK_INFO | Ending assigning project to login user")
        return ResponseEntity.ok(res)
    }

    @Operation(summary = "Assign task to someone employee")
    @PatchMapping("/project/{projectId}/task/{taskId}/employee/{employeeId}")
    fun assignToEmployee(
        @PathVariable(name = "taskId") taskId: Long,
        @PathVariable(name = "projectId") projectId: Long,
        @PathVariable(name = "employeeId") employeeId: Long,
        principal: Principal
    ): ResponseEntity<String> {
        logger.info("EVENT_ASSIGN_TASK_INFO | Start assigning project to someone employee")
        taskService.assignToEmployee(taskId, projectId, employeeId, principal)
        logger.info("EVENT_ASSIGN_TASK_INFO | Ending assigning project to someone employee")
        return ResponseEntity.ok("Successfully assign the task")
    }
}