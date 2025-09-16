package com.laba.it_planner.exception

open class ApiException(message: String, val errorCode: String) : RuntimeException(message)