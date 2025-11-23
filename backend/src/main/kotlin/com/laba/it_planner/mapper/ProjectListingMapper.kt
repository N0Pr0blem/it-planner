package com.laba.it_planner.mapper

import com.laba.it_planner.dto.project.ProjectListingDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.project.Project
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ProjectListingMapper : Mappable<Project, ProjectListingDto>{
}