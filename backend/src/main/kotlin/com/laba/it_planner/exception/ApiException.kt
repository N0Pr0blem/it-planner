package com.laba.it_planner.exception

open class ApiException(messageCode: String, val args: Any) : RuntimeException(messageCode)