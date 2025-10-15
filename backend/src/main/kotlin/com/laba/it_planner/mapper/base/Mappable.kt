package com.laba.it_planner.mapper.base

interface Mappable<E,D> {
    fun toDto(entity: E): D
    fun toEntity(dto: D): E

    fun toDtos(entities: Iterable<E>): List<D>
    fun toEntities(dtos: Iterable<D>): List<E>
}