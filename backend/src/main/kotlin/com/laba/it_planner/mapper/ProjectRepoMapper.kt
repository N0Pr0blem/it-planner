package com.laba.it_planner.mapper

import com.laba.it_planner.dto.repo.ProjectRepoFileDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.project.repository.ProjectRepoFile
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ProjectRepoMapper: Mappable<ProjectRepoFile, ProjectRepoFileDto> {
    @Mapping(target = "projectRepoId", source = "projectRepo.id")
    override fun toDto(entity: ProjectRepoFile): ProjectRepoFileDto
}