package com.laba.it_planner.service.task.impl

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.dto.comment.CommentCreateRequest
import com.laba.it_planner.dto.comment.CommentInfoDto
import com.laba.it_planner.dto.comment.CommentUpdateDto
import com.laba.it_planner.exception.DataException
import com.laba.it_planner.mapper.task.CommentMapper
import com.laba.it_planner.model.task.Comment
import com.laba.it_planner.model.task.Comment_.author
import com.laba.it_planner.model.task.Task
import com.laba.it_planner.model.user.UserInfo
import com.laba.it_planner.repository.task.CommentRepository
import com.laba.it_planner.service.storage.FileService
import com.laba.it_planner.service.task.CommentService
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.util.*


@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val commentMapper: CommentMapper,
    private val messageSource: MessageSource,
    private val fileService: FileService
) : CommentService {
    private val logger = LoggerFactory.getLogger(CommentServiceImpl::class.java.name)

    override fun create(
        task: Task,
        commentCreateRequest: CommentCreateRequest,
        user: UserInfo,
    ): CommentInfoDto {
        val comment = Comment(
            task = task,
            text = commentCreateRequest.text,
            creationDate = LocalDateTime.now(),
            author = user,
        )
        val res = commentRepository.save(comment)
        logger.info("Successfully saved comment: {}", res.toString())

        return commentMapper.toDto(res)
    }

    override fun getAll(task: Task): List<CommentInfoDto> {
        val comments = commentRepository.findAllByTaskId(task.id)
        logger.info("Successfully got {} comments", comments.size)
        setProfileImageByPath(comments)
        return commentMapper.toDtos(comments)
    }

    private fun setProfileImageByPath(comments: List<Comment>) {
        comments.stream().forEach { comment ->
            if (comment.author.profileImage != null) {
                val image = fileService.getFile("users/user_${comment.author.id}/profile/${comment.author.profileImage!!}")
                val encoded: ByteArray = Base64.getEncoder().encode(image)
                comment.author.profileImage = String(encoded, StandardCharsets.UTF_8)
            }
        }
    }

    override fun update(
        task: Task,
        commentUpdateDto: CommentUpdateDto,
        commentId: Long,
        user: UserInfo
    ): CommentInfoDto {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { DataException("error.comment.not_exist", commentId) }
        if (comment.author.id != user.id) {
            throw DataException("error.comment.author", commentId)
        }
        comment.text = commentUpdateDto.text
        val savedComment = commentRepository.save(comment)
        logger.info("Update comment to: {}", savedComment.toString())

        return commentMapper.toDto(savedComment)
    }

    override fun delete(
        task: Task,
        commentId: Long,
        locale: Locale
    ): MessageResponseDto {
        val commentExist = commentRepository.existsById(commentId)
        var result = ""
        if (commentExist) {
            commentRepository.deleteById(commentId)
            result = messageSource.getMessage("message.comment.successfully_delete", arrayOf(commentId), locale)
        } else {
            throw DataException("error.comment.not_exist", commentId)
        }
        logger.info("Delete comment with id: {}", commentId)

        return MessageResponseDto(result)
    }
}