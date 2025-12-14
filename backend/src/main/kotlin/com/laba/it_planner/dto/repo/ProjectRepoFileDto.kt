package com.laba.it_planner.dto.repo

import com.laba.it_planner.model.project.repository.FileType

class ProjectRepoFileDto (
    var id: Long? = null,
    var projectRepoId: Long? = null,
    var type: FileType,
    var name: String?
)