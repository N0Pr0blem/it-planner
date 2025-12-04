package com.example.planner.data.dto.repo

import com.example.planner.data.model.project.repository.FileType

class ProjectRepoFileDto (
    var id: Long? = null,
    var projectRepoId: Long? = null,
    var type: FileType,
    var name: String?
)