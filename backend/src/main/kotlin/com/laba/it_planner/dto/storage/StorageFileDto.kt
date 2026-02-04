package com.laba.it_planner.dto.storage

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.laba.it_planner.utils.files.MimeType
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class StorageFileDto (
    val id: Long? = null,
    var name: String?,
    var creationDate: LocalDateTime?,
    var mimeType: MimeType,
    var storageId: Long,
)