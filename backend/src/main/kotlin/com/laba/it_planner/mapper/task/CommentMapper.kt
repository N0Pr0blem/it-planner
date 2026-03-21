package com.laba.it_planner.mapper.task

import com.laba.it_planner.dto.comment.CommentInfoDto
import com.laba.it_planner.mapper.base.Mappable
import com.laba.it_planner.mapper.user.UserInfoForTaskListingMapper
import com.laba.it_planner.model.task.Comment
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring",uses = [UserInfoForTaskListingMapper::class])
interface CommentMapper : Mappable<Comment, CommentInfoDto>{
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "author", source = "author")
    override fun toDto(entity: Comment): CommentInfoDto
}