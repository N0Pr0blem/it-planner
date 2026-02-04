package com.laba.it_planner.mapper.storage

import com.laba.it_planner.dto.storage.StorageFileDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.storage.StorageFile
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface StorageFileMapper: Mappable<StorageFile, StorageFileDto> {
    @Mapping(target = "storageId", source = "storage.id")
    override fun toDto(entity: StorageFile): StorageFileDto
}
