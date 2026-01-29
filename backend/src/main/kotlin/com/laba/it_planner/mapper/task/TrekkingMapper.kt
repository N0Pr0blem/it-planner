package com.laba.it_planner.mapper.task

import com.laba.it_planner.dto.trekking.TrekkingResponseDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.task.Tracking
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface TrekkingMapper : Mappable<Tracking, TrekkingResponseDto> {
    @Mapping(target = "employeeFirstName", source = "employee.user.firstName")
    @Mapping(target = "employeeSecondName", source = "employee.user.secondName")
    @Mapping(target = "taskDetailsId", source = "taskDetails.id")
    override fun toDto(entity: Tracking): TrekkingResponseDto
}