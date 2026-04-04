package com.laba.it_planner.controller.task

import com.laba.it_planner.dto.trekking.AllTrekkingResponse
import com.laba.it_planner.dto.tracking.TrackingCreationDto
import com.laba.it_planner.dto.tracking.TrackingResponseDto
import com.laba.it_planner.mapper.task.TrackingMapper
import com.laba.it_planner.service.task.TrackingService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1")
class TrackingController(
    private val trackingService: TrackingService,
    private val trackingMapper: TrackingMapper
) {
    private val logger: Logger = Logger.getLogger(TrackingController::class.java.name)

    @Operation(summary = "Create tracking for task")
    @PostMapping("/trekking")
    fun add(
        @RequestBody trackingCreationDto: TrackingCreationDto,
        principal: Principal
    ): ResponseEntity<TrackingResponseDto> {
        logger.info("EVENT_CREATE_TRACKING | Start creation tracking for task")
        val res = trackingService.add(trackingCreationDto, principal)
        logger.info("EVENT_CREATE_TRACKING | Ending creation tracking for task")
        return ResponseEntity.ok(trackingMapper.toDto(res))
    }

    @Operation(summary = "Get all tracking for task")
    @GetMapping("/task/{taskId}/trekking")
    fun getAll(
        @PathVariable("taskId") taskId: Long,
        principal: Principal
    ): ResponseEntity<AllTrekkingResponse> {
        logger.info("EVENT_GET_ALL_TRACKING | Start getting all tracking for task")
        val res = trackingService.findAll(taskId, principal)
        logger.info("EVENT_GET_ALL_TRACKING | Ending getting all tracking for task")
        return ResponseEntity.ok(res)
    }

    @Operation(summary = "Delete tracking of task")
    @DeleteMapping("/trekking/{trekkingId}")
    fun delete(
        @PathVariable("trekkingId") trekkingId: Long,
        principal: Principal
    ): ResponseEntity<String> {
        logger.info("EVENT_DELETE_TRACKING | Start deleting tracking for task")
        trackingService.delete(trekkingId, principal)
        logger.info("EVENT_DELETE_TRACKING | Ending deleting tracking for task")
        return ResponseEntity.ok("Successfully deleted")
    }
}