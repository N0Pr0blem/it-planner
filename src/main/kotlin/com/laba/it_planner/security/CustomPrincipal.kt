package com.laba.it_planner.security;

import java.security.Principal

class CustomPrincipal(
    var id: Long,
    var username: String
) : Principal {

    override fun getName(): String {
        return username
    }
}

