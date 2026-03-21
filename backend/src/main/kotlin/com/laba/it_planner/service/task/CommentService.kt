package com.laba.it_planner.service.task

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.comment.CommentCreateRequest
import com.laba.it_planner.dto.comment.CommentInfoDto
import com.laba.it_planner.dto.comment.CommentUpdateDto
import com.laba.it_planner.model.task.Task
import com.laba.it_planner.model.user.UserInfo
import java.util.Locale

interface CommentService {
    fun create(task: Task, commentCreateRequest: CommentCreateRequest, user: UserInfo): CommentInfoDto
    fun getAll(task: Task): List<CommentInfoDto>
    fun update(task: Task, commentUpdateDto: CommentUpdateDto, commentId: Long, user: UserInfo): CommentInfoDto
    fun delete(task: Task, commentId: Long, locale: Locale): MessageResponseDto
}