package com.laba.it_planner.service.task

import com.laba.it_planner.dto.trekking.AllTrekkingResponse
import com.laba.it_planner.dto.trekking.TrekkingCreationDto
import com.laba.it_planner.mapper.task.TrekkingMapper
import com.laba.it_planner.model.task.Tracking
import com.laba.it_planner.repository.task.TrackingRepository
import com.laba.it_planner.service.project.EmployeeService
import com.laba.it_planner.service.TaskDetailsService
import com.laba.it_planner.service.TrekkingService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class TrackingServiceImpl(
    private val trackingRepository: TrackingRepository,
    private val employeeService: EmployeeService,
    private val taskDetailsService: TaskDetailsService,
    private val trekkingMapper: TrekkingMapper
) : TrekkingService {
    override fun findAll(
        taskId: Long,
        principal: Principal
    ): AllTrekkingResponse {
        val trekkingList = trackingRepository.findAllByTaskId(taskId)
        val hourSum = trekkingList.sumOf { it.hours }

        return AllTrekkingResponse(
            trekkingList = trekkingMapper.toDtos(trekkingList),
            hourSum = hourSum,
        )
    }

    override fun add(
        trekkingCreationDto: TrekkingCreationDto,
        principal: Principal
    ): Tracking {
        return trackingRepository.save(
            Tracking(
                id = null,
                date = trekkingCreationDto.date,
                hours = trekkingCreationDto.hours,
                employee = employeeService.getByUserNameAndProjectId(principal.name, trekkingCreationDto.projectId),
                taskDetails = taskDetailsService.getByTaskId(trekkingCreationDto.taskId),
            )
        )
    }

    override fun delete(trekkingId: Long, principal: Principal) {
        if(trackingRepository.existsById(trekkingId)) {
            trackingRepository.deleteById(trekkingId)
        }
    }
}