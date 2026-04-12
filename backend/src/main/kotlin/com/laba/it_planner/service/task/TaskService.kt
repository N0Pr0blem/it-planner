package com.laba.it_planner.service.task

import TaskPageRequest
import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.task.CreateTaskInfoRequestDto
import com.laba.it_planner.dto.task.TaskArchiveDto
import com.laba.it_planner.dto.task.TaskDeleteDto
import com.laba.it_planner.dto.task.TaskInfoListing
import com.laba.it_planner.dto.task.TaskListingResponse
import com.laba.it_planner.dto.task.UpdateTaskInfoRequestDto
import com.laba.it_planner.model.task.Task
import java.security.Principal
import java.util.Locale

interface TaskService {
    fun get(id: Long, principal: Principal): Task
    fun getAll(projectId: Long,pageRequest:TaskPageRequest, principal: Principal): TaskListingResponse
    fun add(createTaskInfoRequestDto: CreateTaskInfoRequestDto, principal: Principal): Task
    fun update(taskId: Long, updateTaskInfoRequestDto: UpdateTaskInfoRequestDto, principal: Principal): Task
    fun delete(id: Long, principal: Principal)
    fun assignToMe(taskId: Long, projectId: Long, principal: Principal,locale: Locale): String
    fun getDescription(taskId: Long, principal: Principal): MessageResponseDto
    fun getMy(principal: Principal): List<TaskInfoListing>?
    fun assignToEmployee(taskId: Long, projectId: Long, employeeId: Long, principal: Principal)
    fun getByTaskId(taskId: Long): Task
    fun save(task: Task): Task
    fun hideTask(archiveDto: TaskArchiveDto, locale: Locale): String
    fun hideTask(taskId: Long, locale: Locale): String
    fun deleteAll(deleteDto: TaskDeleteDto, principal: Principal)
}