package com.example.planner.data.mapper

import com.example.planner.data.dto.repo.ProjectRepoFileDto
import com.example.planner.domain.model.RepoFile

fun ProjectRepoFileDto.toDomain(): RepoFile =
    RepoFile(
        id = id ?: 0L,
        projectRepoId = projectRepoId,
        type = type.name,
        name = name.orEmpty()
    )
