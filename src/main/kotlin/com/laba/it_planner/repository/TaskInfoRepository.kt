package com.laba.it_planner.repository

import com.laba.it_planner.model.task.TaskInfo
import org.springframework.data.jpa.repository.JpaRepository

interface TaskInfoRepository: JpaRepository<TaskInfo, Long> {
}