package com.laba.it_planner.service.task.impl

import com.laba.it_planner.dto.trekking.AllTrekkingResponse
import com.laba.it_planner.dto.tracking.TrackingCreationDto
import com.laba.it_planner.dto.tracking.TrackingResponseDto
import com.laba.it_planner.mapper.task.TrackingMapper
import com.laba.it_planner.model.task.Tracking
import com.laba.it_planner.repository.task.TrackingRepository
import com.laba.it_planner.service.project.EmployeeService
import com.laba.it_planner.service.task.TrackingService
import com.laba.it_planner.service.task.TaskService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class TrackingServiceImpl(
    private val trackingRepository: TrackingRepository,
    private val employeeService: EmployeeService,
    private val trackingMapper: TrackingMapper,
    private val taskService: TaskService,
) : TrackingService {
    override fun findAll(
        taskId: Long,
        principal: Principal
    ): AllTrekkingResponse {
        val trekkingList = trackingRepository.findAllByTaskId(taskId)
        val hourSum = trekkingList.sumOf { it.hours }

        return AllTrekkingResponse(
            trekkingList = trackingMapper.toDtos(trekkingList),
            hourSum = hourSum,
        )
    }

    override fun add(
        trackingCreationDto: TrackingCreationDto,
        principal: Principal
    ): Tracking {
        return trackingRepository.save(
            Tracking(
                id = null,
                date = trackingCreationDto.date,
                hours = trackingCreationDto.hours,
                employee = employeeService.getByUserNameAndProjectId(principal.name, trackingCreationDto.projectId),
                taskDetails = taskService.getByTaskId(trackingCreationDto.taskId),
            )
        )
    }

    override fun delete(trekkingId: Long, principal: Principal) {
        if(trackingRepository.existsById(trekkingId)) {
            trackingRepository.deleteById(trekkingId)
        }
    }

    override fun getAllUsersTracking(principal: Principal):AllTrekkingResponse {
        val trekkingList = trackingRepository.findAllByUsername(principal.name)

        val hourSum = trekkingList.sumOf { it.hours }

        return AllTrekkingResponse(
            trekkingList = trackingMapper.toDtos(trekkingList),
            hourSum = hourSum,
        )
    }
}