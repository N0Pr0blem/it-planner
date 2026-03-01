package com.laba.it_planner.mapper.project

import com.laba.it_planner.dto.project.ProjectListingDto
import com.laba.it_planner.dto.storage.StorageFileDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.storage.StorageFile
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ProjectListingMapper : Mappable<Project, ProjectListingDto>{
    @Mapping(target = "storageId", source = "storage.id")
    override fun toDto(entity: Project): ProjectListingDto
}