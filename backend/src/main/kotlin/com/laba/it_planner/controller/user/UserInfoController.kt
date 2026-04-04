package com.laba.it_planner.controller.user

import com.laba.it_planner.controller.task.TaskInfoController
import com.laba.it_planner.dto.userInfo.UserInfoPatchDto
import com.laba.it_planner.dto.userInfo.UserInfoResponseDto
import com.laba.it_planner.mapper.user.UserInfoMapper
import com.laba.it_planner.service.user.UserInfoService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.util.logging.Logger

@RestController
@RequestMapping("/api/v1/profile")
class UserInfoController(
    private val userInfoService: UserInfoService,
    private val userInfoMapper: UserInfoMapper
) {
    private val logger: Logger = Logger.getLogger(UserInfoController::class.java.name)

    @PatchMapping
    @Operation(summary = "Update user details")
    fun updateUserDetails(
        @RequestPart("secondName") secondName: String?,
        @RequestPart("lastName") lastName: String?,
        @RequestPart("file") multipartFile: MultipartFile?,
        principal: Principal
    ): ResponseEntity<UserInfoResponseDto> {
        logger.info("EVENT_UPDATE_USER | Start updating user")
        val result = userInfoService.update(
            UserInfoPatchDto(secondName = secondName, lastName = lastName),
            multipartFile,
            principal
        )
        logger.info("EVENT_UPDATE_USER | Ending updating user")
        return ResponseEntity.ok(userInfoMapper.toDto(result))
    }

    @GetMapping
    @Operation(summary = "Get user info")
    fun getUserInfo(principal: Principal): ResponseEntity<UserInfoResponseDto> {
        logger.info("EVENT_GET_USER | Start getting user info")
        val result = userInfoService.getInfo(principal)
        logger.info("EVENT_GET_USER | Ending getting user info")
        return ResponseEntity.ok(userInfoMapper.toDto(result))
    }
}