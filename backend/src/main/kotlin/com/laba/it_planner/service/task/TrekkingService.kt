package com.laba.it_planner.service

import com.laba.it_planner.dto.trekking.AllTrekkingResponse
import com.laba.it_planner.dto.trekking.TrekkingCreationDto
import com.laba.it_planner.model.task.Tracking
import java.security.Principal

interface TrekkingService {
    fun findAll(taskId: Long, principal: Principal): AllTrekkingResponse
    fun add(trekkingCreationDto: TrekkingCreationDto, principal: Principal): Tracking
    fun delete(trekkingId: Long, principal: Principal)
}