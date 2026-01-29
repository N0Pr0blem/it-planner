package com.laba.it_planner.mapper.project

import com.laba.it_planner.dto.project.ProjectCreateResponseDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.project.Project
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ProjectCreationMapper : Mappable<Project, ProjectCreateResponseDto>