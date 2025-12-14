package com.laba.it_planner.service

import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.task.TaskDetails
import java.security.Principal

interface TaskDetailsService {
    fun getEmpty(project: Project, principal: Principal, description: String): TaskDetails
    fun getByTaskId(taskId: Long): TaskDetails
    fun save(taskDetails: TaskDetails): TaskDetails
}
