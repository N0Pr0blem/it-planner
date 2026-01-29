package com.laba.it_planner.service.user

import com.laba.it_planner.dto.userInfo.UserInfoPatchDto
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.model.user.UserInfo
import com.laba.it_planner.repository.user.UserInfoRepository
import com.laba.it_planner.service.FileService
import com.laba.it_planner.service.UserInfoService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.charset.StandardCharsets
import java.security.Principal
import java.util.*

@Service
class UserInfoServiceImpl(
    private val userInfoRepository: UserInfoRepository,
    private val fileService: FileService,
) : UserInfoService {

    override fun update(
        userInfoPatchDto: UserInfoPatchDto,
        multipartFile: MultipartFile?,
        principal: Principal
    ): UserInfo {
        val userInfoOpt = userInfoRepository.findByUsername(principal.name)
        if (userInfoOpt.isPresent) {
            val userInfo = userInfoOpt.get()
            if (userInfoPatchDto.secondName != null) userInfo.secondName = userInfoPatchDto.secondName
            if (userInfoPatchDto.lastName != null) userInfo.lastName = userInfoPatchDto.lastName
            if (multipartFile != null) {
                val path = userInfo.email + FileType.PROFILE.prefix + multipartFile.originalFilename
                userInfo.profileImage=fileService.saveFile(path, multipartFile);
            }
            return userInfoRepository.save(userInfo)
        }
        else throw DataException("error.user.username.not_found",principal.name)
    }

    override fun getInfo(principal: Principal) : UserInfo{
        val userInfoOpt = userInfoRepository.findByUsername(principal.name)
        return getUserInfo(userInfoOpt)
    }

    override fun getInfo(id: Long): UserInfo {
        val userInfoOpt = userInfoRepository.findById(id)
        return getUserInfo(userInfoOpt)
    }

    override fun getUserInfo(principal: Principal): UserInfo {
        return getUserInfo(principal.name)
    }

    override fun getUserInfo(username: String): UserInfo {
        val userInfoOpt = userInfoRepository.findByUsername(username)
        return userInfoOpt.get()
    }

    private fun getUserInfo(userInfoOpt: Optional<UserInfo>): UserInfo {
        if (userInfoOpt.isPresent) {
            val userInfo = userInfoOpt.get()
            if(userInfo.profileImage!=null) {
                val image = fileService.getFile(userInfo.profileImage!!)
                val encoded: ByteArray = Base64.getEncoder().encode(image)
                userInfo.profileImage=String(encoded, StandardCharsets.UTF_8)
            }
            return userInfo
        }
        else throw DataException("error.user.not_found","")
    }

}