package com.laba.it_planner.repository

import com.laba.it_planner.model.task.Trekking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TrekkingRepository : JpaRepository<Trekking, Long> {
    @Query(
        """
        SELECT t.* FROM task_trekking t 
        LEFT JOIN task_details td ON td.id = t.task_details_id
        LEFT JOIN task_info ti ON ti.task_details_id = td.id
        WHERE ti.id = :task_id
    """, nativeQuery = true
    )
    fun findAllByTaskId(@Param("task_id") taskId: Long):List<Trekking>
}