package com.laba.it_planner.service

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.task.CreateTaskInfoRequestDto
import com.laba.it_planner.dto.task.TaskInfoListing
import com.laba.it_planner.dto.task.UpdateTaskInfoRequestDto
import com.laba.it_planner.model.task.TaskInfo
import java.security.Principal

interface TaskInfoService {
    fun get(id: Long, principal: Principal): TaskInfo
    fun getAll(projectId: Long, principal: Principal): List<TaskInfoListing>
    fun add(createTaskInfoRequestDto: CreateTaskInfoRequestDto, principal: Principal): TaskInfo
    fun update(taskId: Long, updateTaskInfoRequestDto: UpdateTaskInfoRequestDto, principal: Principal): TaskInfo
    fun delete(id: Long, principal: Principal)
    fun assignToMe(taskId: Long, projectId: Long, principal: Principal)
    fun getDescription(taskId: Long, principal: Principal): MessageResponseDto
}