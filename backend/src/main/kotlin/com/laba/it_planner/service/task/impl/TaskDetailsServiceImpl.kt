package com.laba.it_planner.service.task

import com.laba.it_planner.exception.ApiException
import com.laba.it_planner.model.project.Project
import com.laba.it_planner.repository.task.TaskDetailsRepository
import com.laba.it_planner.service.project.EmployeeService
import com.laba.it_planner.service.FileService
import com.laba.it_planner.service.TaskDetailsService
import org.springframework.stereotype.Service
import java.security.Principal
import java.util.*

@Service
class TaskDetailsServiceImpl(
    private val taskDetailsRepository: TaskDetailsRepository,
    private val employeeService: EmployeeService,
    private val fileService: FileService
) : TaskDetailsService {
    override fun getEmpty(project: Project, principal: Principal, description: String): TaskDetails {
        val employee = employeeService.getByUserNameAndProjectId(principal.name, project.id!!)
        val taskFolderName = UUID.randomUUID().toString()
        try {
            fileService.createDescriptionFileForTask(project.name!!, principal, taskFolderName, description)
            return taskDetailsRepository.save(
                TaskDetails(
                    fromUser = employee,
                    descriptionFile = taskFolderName
                )
            )
        } catch (e: ApiException) {
            return TaskDetails(null,employee,null,null,null)
        }
    }


    override fun getByTaskId(taskId: Long): TaskDetails {
        return taskDetailsRepository.getByTaskInfoId(taskId)
    }

    override fun save(taskDetails: TaskDetails): TaskDetails {
        return taskDetailsRepository.save(taskDetails)
    }

}