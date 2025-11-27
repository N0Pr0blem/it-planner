package com.laba.it_planner.service.impl

import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.task.TaskDetails
import com.laba.it_planner.repository.TaskDetailsRepository
import com.laba.it_planner.service.EmployeeService
import com.laba.it_planner.service.FileService
import com.laba.it_planner.service.TaskDetailsService
import org.springframework.stereotype.Service
import java.security.Principal
import java.util.UUID

@Service
class TaskDetailsServiceImpl(
    private val taskDetailsRepository: TaskDetailsRepository,
    private val employeeService: EmployeeService,
    private val fileService: FileService
) : TaskDetailsService {
    override fun getEmpty(project: Project, principal: Principal, description: String): TaskDetails {
        val employee = employeeService.getByUserNameAndProjectId(principal.name, project.id!!)
        return taskDetailsRepository.save(
            TaskDetails(
                fromUser = employee,
                descriptionFile = fileService.createDescriptionFileForTask(project.name!!, principal, UUID.randomUUID().toString(), description)
            )
        )
    }

    override fun getByTaskId(taskId: Long): TaskDetails {
        return taskDetailsRepository.getByTaskInfoId(taskId)
    }

    override fun save(taskDetails: TaskDetails): TaskDetails {
        return taskDetailsRepository.save(taskDetails)
    }

}