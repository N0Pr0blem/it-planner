package com.laba.it_planner.utils.feature

import org.springframework.stereotype.Service

@Service
class FeatureToggleService(
    private val featureRepository: FeatureRepository,
) {
    fun isEnabled(name:String):Boolean {
        val toggleOpt = featureRepository.findByName(name)
        if(toggleOpt.isPresent) {
            val res = toggleOpt.get().enabled
            println("$name is enabled: $res")
            return res
        }
        else {
            println("Toggle with name - \'$name\' not found")
            return false
        }
    }
}