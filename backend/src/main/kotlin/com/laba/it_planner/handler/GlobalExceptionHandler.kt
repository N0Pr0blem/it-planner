package com.laba.it_planner.handler

import com.laba.it_planner.dto.MessageResponseDto
import com.laba.it_planner.exception.ApiException
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.LinkedList
import java.util.Locale
import java.util.function.Consumer


@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource
) {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<LinkedList<MessageResponseDto>> {
        val errors: LinkedList<MessageResponseDto> = LinkedList()
        ex.bindingResult
            .fieldErrors
            .forEach(Consumer { error: FieldError? -> errors.add(MessageResponseDto(error!!.defaultMessage?:"")) })
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException, locale: Locale): ResponseEntity<MessageResponseDto> {
        val argsArray = when (e.args) {
            is Array<*> -> e.args
            else -> arrayOf(e.args)
        }

        val message = messageSource.getMessage(e.message!!, argsArray, locale)
        val response = MessageResponseDto(message)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }
}