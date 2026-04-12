package com.laba.it_planner.dto.task;

data class TaskListingResponse(
    var tasks: List<TaskInfoListing>,
    var pages: Int,
    var size: Int,
    var archived: Boolean,
    var pageNumber: Int,
)
