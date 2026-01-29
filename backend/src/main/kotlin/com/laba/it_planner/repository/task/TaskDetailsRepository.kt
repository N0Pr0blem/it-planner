package com.laba.it_planner.repository.task

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TaskDetailsRepository: JpaRepository<TaskDetails, Long> {
    @Query("""
        Select td.* from task_details td
         left join task_info ti on ti.task_details_id = td.id 
         where ti.id=:task_id
    """, nativeQuery = true)
    fun getByTaskInfoId(@Param("task_id") taskId: Long): TaskDetails
}