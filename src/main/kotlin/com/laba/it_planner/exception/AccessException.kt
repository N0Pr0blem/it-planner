package com.laba.it_planner.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class AccessException(message: String,errorCode:String): ApiException(message, errorCode)