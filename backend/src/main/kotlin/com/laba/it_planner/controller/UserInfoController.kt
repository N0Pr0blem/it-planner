package com.laba.it_planner.controller

import com.laba.it_planner.dto.userInfo.UserInfoPatchDto
import com.laba.it_planner.dto.userInfo.UserInfoResponseDto
import com.laba.it_planner.mapper.UserInfoMapper
import com.laba.it_planner.service.UserInfoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/api/v1/profile")
class UserInfoController(
    private val userInfoService: UserInfoService,
    private val userInfoMapper: UserInfoMapper
) {

    @PatchMapping
    @Operation(summary = "Update user details")
    fun updateUserDetails(
        @RequestBody userInfoPatchDto: UserInfoPatchDto,
        @RequestPart multipartFile: MultipartFile?,
        principal: Principal
    ): ResponseEntity<UserInfoResponseDto> {
        val result = userInfoService.update(userInfoPatchDto,multipartFile,principal)
        return ResponseEntity.ok(userInfoMapper.toDto(result))
    }

    @GetMapping
    @Operation(summary = "Get user info")
    fun getUserInfo(principal: Principal): ResponseEntity<UserInfoResponseDto> {
        val result = userInfoService.getInfo(principal)
        return ResponseEntity.ok(userInfoMapper.toDto(result))
    }
}