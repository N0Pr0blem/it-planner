package com.laba.it_planner.controller.task

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.comment.CommentCreateRequest
import com.laba.it_planner.dto.comment.CommentInfoDto
import com.laba.it_planner.dto.comment.CommentUpdateDto
import com.laba.it_planner.model.task.Task
import com.laba.it_planner.model.user.UserInfo
import com.laba.it_planner.service.task.CommentService
import com.laba.it_planner.service.task.TaskService
import com.laba.it_planner.service.user.UserInfoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/task/{taskId}/comment")
class CommentController(
    private val commentService: CommentService,
    private val taskService: TaskService,
    private val userService: UserInfoService,
) {
    private val logger: Logger = Logger.getLogger(CommentController::class.java.name)

    @ModelAttribute("task")
    fun getTaskAttribute(
        @PathVariable taskId: Long,
        principal: Principal
    ): Task = taskService.get(taskId, principal)

    @ModelAttribute("user")
    fun getUserAttribute(principal: Principal): UserInfo = userService.getUserInfo(principal)

    @PostMapping
    @Operation(summary = "Create new comment")
    fun createComment(
        @RequestBody commentCreateRequest: CommentCreateRequest,
        @ModelAttribute("task") task: Task,
        @ModelAttribute("user") user: UserInfo,
    ): ResponseEntity<CommentInfoDto> {
        logger.info("EVENT_CREATE_COMMENT | Start creating comment")
        val response = commentService.create(task, commentCreateRequest, user)
        logger.info("EVENT_CREATE_COMMENT | Finish creating comment")

        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping
    @Operation(summary = "Get all comments")
    fun getComments(
        @ModelAttribute("task") task: Task,
    ): ResponseEntity<List<CommentInfoDto>> {
        logger.info("EVENT_GET_ALL_COMMENTS | Start getting comment")
        val response = commentService.getAll(task)
        logger.info("EVENT_GET_ALL_COMMENTS | Finish getting comment")

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "Update comment")
    fun updateComment(
        @ModelAttribute("task") task: Task,
        @ModelAttribute("user") user: UserInfo,
        @PathVariable commentId: Long,
        @RequestBody commentUpdateDto: CommentUpdateDto,
    ): ResponseEntity<CommentInfoDto> {
        logger.info("EVENT_UPDATE_COMMENT | Start updating comment")
        val response = commentService.update(task, commentUpdateDto, commentId, user)
        logger.info("EVENT_UPDATE_COMMENT | Finish updating comment")

        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment")
    fun deleteComment(
        @ModelAttribute("task") task: Task,
        @PathVariable commentId: Long,
        locale: Locale
    ): ResponseEntity<MessageResponseDto> {
        logger.info("EVENT_DELETE_COMMENT | Start deleting comment")
        val response = commentService.delete(task, commentId, locale)
        logger.info("EVENT_DELETE_COMMENT | Finish deleting comment")

        return ResponseEntity(response, HttpStatus.OK)
    }
}