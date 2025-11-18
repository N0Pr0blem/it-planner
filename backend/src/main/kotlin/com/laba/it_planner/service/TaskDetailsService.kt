package com.laba.it_planner.service

import com.laba.it_planner.model.task.TaskDetails
import java.security.Principal

interface TaskDetailsService {
    fun getEmpty(principal: Principal): TaskDetails

}
