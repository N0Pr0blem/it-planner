package com.laba.it_planner.service.task

import com.laba.it_planner.dto.trekking.AllTrekkingResponse
import com.laba.it_planner.dto.tracking.TrackingCreationDto
import com.laba.it_planner.dto.tracking.TrackingResponseDto
import com.laba.it_planner.model.task.Tracking
import java.security.Principal

interface TrackingService {
    fun findAll(taskId: Long, principal: Principal): AllTrekkingResponse
    fun add(trackingCreationDto: TrackingCreationDto, principal: Principal): Tracking
    fun delete(trekkingId: Long, principal: Principal)
    fun getAllUsersTracking(principal: Principal):AllTrekkingResponse
}