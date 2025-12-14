package com.laba.it_planner.controller

import com.laba.it_planner.dto.trekking.AllTrekkingResponse
import com.laba.it_planner.dto.trekking.TrekkingCreationDto
import com.laba.it_planner.dto.trekking.TrekkingResponseDto
import com.laba.it_planner.mapper.TrekkingMapper
import com.laba.it_planner.service.TrekkingService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1")
class TrekkingController(
    private val trekkingService: TrekkingService,
    private val trekkingMapper: TrekkingMapper
) {
    @Operation(summary = "Create trekking for task")
    @PostMapping("/trekking")
    fun add(
        @RequestBody trekkingCreationDto: TrekkingCreationDto,
        principal: Principal
    ): ResponseEntity<TrekkingResponseDto> {
        val res = trekkingService.add(trekkingCreationDto, principal)
        return ResponseEntity.ok(trekkingMapper.toDto(res))
    }

    @Operation(summary = "Get all trekking for task")
    @GetMapping("/task/{taskId}/trekking")
    fun getAll(
        @PathVariable("taskId") taskId: Long,
        principal: Principal
    ): ResponseEntity<AllTrekkingResponse> {
        val res = trekkingService.findAll(taskId, principal)
        return ResponseEntity.ok(res)
    }

    @Operation(summary = "Delete trekking of task")
    @DeleteMapping("/trekking/{trekkingId}")
    fun delete(
        @PathVariable("trekkingId") trekkingId: Long,
        principal: Principal
    ): ResponseEntity<String> {
        trekkingService.delete(trekkingId, principal)
        return ResponseEntity.ok("Successfully deleted")
    }
}