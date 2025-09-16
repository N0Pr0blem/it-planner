package com.laba.it_planner.mapper

import com.laba.it_planner.dto.oauth.RegisterResponseDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.model.user.OauthUser
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component

@Component
class RegisterMapper(
    private val modelMapper: ModelMapper
) : Mappable<OauthUser, RegisterResponseDto> {

    override fun toDto(entity: OauthUser): RegisterResponseDto {
        return modelMapper.map(entity, RegisterResponseDto::class.java)
    }

    override fun toEntity(dto: RegisterResponseDto): OauthUser {
        return modelMapper.map(dto, OauthUser::class.java)
    }

    override fun toDtos(entities: Iterable<OauthUser>): List<RegisterResponseDto> {
        return entities.map { toDto(it) }
    }

    override fun toEntities(dtos: Iterable<RegisterResponseDto>): List<OauthUser> {
        return dtos.map { toEntity(it) }
    }
}