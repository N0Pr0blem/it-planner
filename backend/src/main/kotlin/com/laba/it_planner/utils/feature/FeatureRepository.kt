package com.laba.it_planner.utils.feature

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface FeatureRepository: JpaRepository<FeatureModel, Long> {
    fun findByName(name: String): Optional<FeatureModel>
}