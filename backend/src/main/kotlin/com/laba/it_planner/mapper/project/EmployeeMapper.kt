package com.laba.it_planner.mapper.project

import com.laba.it_planner.dto.employee.EmployeeResponseDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.mapper.user.UserMainInfoMapper
import com.laba.it_planner.model.project.Employee
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [UserMainInfoMapper::class])
interface EmployeeMapper : Mappable<Employee, EmployeeResponseDto> {
}