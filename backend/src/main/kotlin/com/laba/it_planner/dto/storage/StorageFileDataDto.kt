package com.laba.it_planner.dto.storage

data class StorageFileDataDto(
    var mimeType: String,
    var encodedFileName: String,
    var size: String,
    var content: ByteArray,
)