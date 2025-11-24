package com.laba.it_planner.repository.projection

interface TaskListingProjection {
    fun getId(): Long
    fun getName(): String
    fun getIsCompleted(): Boolean?
    fun getFirstName(): String
    fun getSecondName(): String
    fun getProfileImage(): String?
}