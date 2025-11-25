package com.laba.it_planner.repository

import com.laba.it_planner.model.task.TaskDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TaskDetailsRepository: JpaRepository<TaskDetails, Long> {
    @Query("""
        Select * from task_details td where td.id=:task_id
    """, nativeQuery = true)
    fun getByTaskInfoId(@Param("task_id") taskId: Long): TaskDetails
}