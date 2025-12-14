package com.laba.it_planner.handler

import com.laba.it_planner.exception.ApiException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException): ResponseEntity<Map<String, String>> {
        val response = mapOf("error_code" to e.errorCode, "message" to e.message.orEmpty())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(response)
    }
}