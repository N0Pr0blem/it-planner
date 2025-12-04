package com.example.planner.data.dto.repo

import com.example.planner.data.model.repo.FileType

class ProjectRepoFileDto (
    var id: Long? = null,
    var projectRepoId: Long? = null,
    var type: FileType,
    var name: String?
)