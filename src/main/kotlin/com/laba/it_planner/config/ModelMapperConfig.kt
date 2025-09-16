package com.laba.it_planner.config

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelMapperConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper.configuration.apply {
            matchingStrategy = MatchingStrategies.STRICT
            isSkipNullEnabled = true
            isFieldMatchingEnabled = true
            fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE
        }
        return modelMapper
    }
}