package com.laba.it_planner.mapper.user

import com.laba.it_planner.dto.userInfo.UserInfoForTaskDto
import com.laba.it_planner.model.user.UserInfo
import com.laba.it_planner.service.FileService
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class UserInfoForTaskListingMapper(
    private val fileService: FileService
) {
    fun toDto(entity: UserInfo?): UserInfoForTaskDto {
        val result = UserInfoForTaskDto()
        if(entity != null) {
            if (entity.profileImage != null) {
                val image = fileService.getFile(entity.profileImage!!)
                val encoded: ByteArray = Base64.getEncoder().encode(image)
                result.profileImage = String(encoded, StandardCharsets.UTF_8)
            }
            if (entity.firstName != null) {
                result.firstName = entity.firstName
            }
            if (entity.secondName != null) {
                result.secondName = entity.secondName
            }
        }

        return result
    }

    fun toDtos(entities: Iterable<UserInfo>): List<UserInfoForTaskDto> {
        return entities.map {entity-> this.toDto(entity)}
    }
}