package com.laba.it_planner.mapper.task

import com.laba.it_planner.dto.tracking.TrackingResponseDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.task.Tracking
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface TrackingMapper : Mappable<Tracking, TrackingResponseDto> {
    @Mapping(target = "employeeFirstName", source = "employee.user.firstName")
    @Mapping(target = "employeeSecondName", source = "employee.user.secondName")
    @Mapping(target = "taskDetailsId", source = "taskDetails.id")
    @Mapping(target = "taskName", source = "taskDetails.name")
    override fun toDto(entity: Tracking): TrackingResponseDto
}