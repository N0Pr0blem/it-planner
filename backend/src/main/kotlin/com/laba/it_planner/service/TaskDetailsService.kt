package com.laba.it_planner.service

import com.laba.it_planner.model.task.TaskDetails
import java.security.Principal

interface TaskDetailsService {
    fun getEmpty(projectId: Long, principal: Principal): TaskDetails

}
