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

@RestController
@RequestMapping("/api/v1")
class TrekkingController(
    private val trackingService: TrackingService,
    private val trackingMapper: TrackingMapper
) {
    @Operation(summary = "Create trekking for task")
    @PostMapping("/trekking")
    fun add(
        @RequestBody trackingCreationDto: TrackingCreationDto,
        principal: Principal
    ): ResponseEntity<TrackingResponseDto> {
        val res = trackingService.add(trackingCreationDto, principal)
        return ResponseEntity.ok(trackingMapper.toDto(res))
    }

    @Operation(summary = "Get all trekking for task")
    @GetMapping("/task/{taskId}/trekking")
    fun getAll(
        @PathVariable("taskId") taskId: Long,
        principal: Principal
    ): ResponseEntity<AllTrekkingResponse> {
        val res = trackingService.findAll(taskId, principal)
        return ResponseEntity.ok(res)
    }

    @Operation(summary = "Delete trekking of task")
    @DeleteMapping("/trekking/{trekkingId}")
    fun delete(
        @PathVariable("trekkingId") trekkingId: Long,
        principal: Principal
    ): ResponseEntity<String> {
        trackingService.delete(trekkingId, principal)
        return ResponseEntity.ok("Successfully deleted")
    }
}