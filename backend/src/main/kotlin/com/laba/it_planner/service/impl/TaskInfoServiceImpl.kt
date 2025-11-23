package com.laba.it_planner.service.impl

import com.laba.it_planner.dto.task.CreateTaskInfoRequestDto
import com.laba.it_planner.dto.task.UpdateTaskInfoRequestDto
import com.laba.it_planner.exception.AccessException
import com.laba.it_planner.model.task.TaskInfo
import com.laba.it_planner.repository.TaskInfoRepository
import com.laba.it_planner.service.EmployeeService
import com.laba.it_planner.service.ProjectService
import com.laba.it_planner.service.TaskDetailsService
import com.laba.it_planner.service.TaskInfoService
import org.springframework.stereotype.Service
import java.security.Principal
import java.time.LocalDateTime

@Service
class TaskInfoServiceImpl(
    private val taskInfoRepository: TaskInfoRepository,
    private val employeeService: EmployeeService,
    private val projectService: ProjectService,
    private val taskDetailService: TaskDetailsService,
) : TaskInfoService {
    override fun get(id: Long, principal: Principal): TaskInfo {
        val taskInfoOpt = taskInfoRepository.findById(id)
        if (taskInfoOpt.isPresent) {
            val taskInfo = taskInfoOpt.get()
            if (employeeService.checkPermission(taskInfo.project.id!!, principal)) {
                return taskInfo
            } else {
                throw AccessException("Access denied", "FORBIDDEN")
            }
        } else {
            throw AccessException("Task not found", "NOT_FOUND")
        }
    }

    override fun getAll(
        projectId: Long,
        principal: Principal
    ): List<TaskInfo> {
        if (employeeService.checkPermission(projectId, principal)) {
            return taskInfoRepository.findAllByProjectId(projectId)
        } else {
            throw AccessException("Access denied", "FORBIDDEN")
        }
    }

    override fun add(createTaskInfoRequestDto: CreateTaskInfoRequestDto, principal: Principal): TaskInfo {
        if (employeeService.checkPermission(createTaskInfoRequestDto.projectId, principal)) {
            return taskInfoRepository.save(
                TaskInfo(
                    name = createTaskInfoRequestDto.name,
                    urgency = createTaskInfoRequestDto.urgency,
                    complexity = createTaskInfoRequestDto.complexity,
                    project = projectService.get(createTaskInfoRequestDto.projectId),
                    taskDetails = taskDetailService.getEmpty(createTaskInfoRequestDto.projectId,principal),
                    creationDate = LocalDateTime.now(),
                )
            )
        } else {
            throw AccessException("Access denied", "FORBIDDEN")
        }
    }

    override fun update(
        taskId: Long,
        updateTaskInfoRequestDto: UpdateTaskInfoRequestDto,
        principal: Principal
    ): TaskInfo {
        val task = get(taskId, principal)
        if (updateTaskInfoRequestDto.name != null) task.name = updateTaskInfoRequestDto.name!!
        if (updateTaskInfoRequestDto.isCompleted != null) task.isCompleted = updateTaskInfoRequestDto.isCompleted!!
        if (updateTaskInfoRequestDto.complexity != null) task.complexity = updateTaskInfoRequestDto.complexity!!
        if (updateTaskInfoRequestDto.urgency != null) task.urgency = updateTaskInfoRequestDto.urgency!!

        return taskInfoRepository.save(task)
    }

    override fun delete(id: Long, principal: Principal) {
        if (taskInfoRepository.existsById(id)) {
            taskInfoRepository.deleteById(id)
        }
    }
}