package com.example.planner.data.dto.repo

import com.example.planner.data.model.repo.FileType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProjectRepoFileDto (
    var id: Long? = null,
    @Json(name = "project_repo_id")
    var projectRepoId: Long? = null,
    var type: FileType,
    var name: String?
)