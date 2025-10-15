package com.laba.it_planner.security

import java.util.*

class TokenDetails (
    var token:String,
    var issuedAt: Date? = null,
    var expiresAt: Date? = null,
)