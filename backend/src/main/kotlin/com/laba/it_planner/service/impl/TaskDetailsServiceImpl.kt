package com.laba.it_planner.service.impl

import com.laba.it_planner.model.task.TaskDetails
import com.laba.it_planner.repository.TaskDetailsRepository
import com.laba.it_planner.service.EmployeeService
import com.laba.it_planner.service.OauthService
import com.laba.it_planner.service.TaskDetailsService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class TaskDetailsServiceImpl(
    private val taskDetailsRepository: TaskDetailsRepository,
    private val employeeService: EmployeeService,
) : TaskDetailsService {
    override fun getEmpty(projectId: Long, principal: Principal): TaskDetails {
        val employee = employeeService.getByUserNameAndProjectId(principal.name,projectId)
        return taskDetailsRepository.save(TaskDetails(fromUser = employee))
    }

}