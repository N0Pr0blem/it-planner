package com.laba.it_planner.repository

import com.laba.it_planner.model.task.TaskFile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TaskFileRepository: JpaRepository<TaskFile, Long> {
    @Query("""
        select tf.*
from task_info ti
         left join task_file tf ON ti.id = tf.task_id
where ti.id=:task_id
        """, nativeQuery = true)
    fun findAllByTaskId(@Param("task_id") taskId: Long):List<TaskFile>?
}