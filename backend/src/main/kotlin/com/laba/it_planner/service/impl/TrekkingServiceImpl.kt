package com.laba.it_planner.service.impl

import com.laba.it_planner.dto.trekking.AllTrekkingResponse
import com.laba.it_planner.dto.trekking.TrekkingCreationDto
import com.laba.it_planner.mapper.TrekkingMapper
import com.laba.it_planner.model.task.Trekking
import com.laba.it_planner.repository.TrekkingRepository
import com.laba.it_planner.service.EmployeeService
import com.laba.it_planner.service.TaskDetailsService
import com.laba.it_planner.service.TrekkingService
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class TrekkingServiceImpl(
    private val trekkingRepository: TrekkingRepository,
    private val employeeService: EmployeeService,
    private val taskDetailsService: TaskDetailsService,
    private val trekkingMapper: TrekkingMapper
) : TrekkingService {
    override fun findAll(
        taskId: Long,
        principal: Principal
    ): AllTrekkingResponse {
        val trekkingList = trekkingRepository.findAllByTaskId(taskId)
        val hourSum = trekkingList.sumOf { it.hours }

        return AllTrekkingResponse(
            trekkingList = trekkingMapper.toDtos(trekkingList),
            hourSum = hourSum,
        )
    }

    override fun add(
        trekkingCreationDto: TrekkingCreationDto,
        principal: Principal
    ): Trekking {
        return trekkingRepository.save(
            Trekking(
                id = null,
                date = trekkingCreationDto.date,
                hours = trekkingCreationDto.hours,
                employee = employeeService.getByUserNameAndProjectId(principal.name, trekkingCreationDto.projectId),
                taskDetails = taskDetailsService.getByTaskId(trekkingCreationDto.taskId),
            )
        )
    }

    override fun delete(trekkingId: Long, principal: Principal) {
        if(trekkingRepository.existsById(trekkingId)) {
            trekkingRepository.deleteById(trekkingId)
        }
    }
}