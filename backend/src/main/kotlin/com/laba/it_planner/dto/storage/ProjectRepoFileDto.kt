package com.laba.it_planner.dto.repo

class ProjectRepoFileDto (
    var id: Long? = null,
    var projectRepoId: Long? = null,
    var type: FileType,
    var name: String?
)