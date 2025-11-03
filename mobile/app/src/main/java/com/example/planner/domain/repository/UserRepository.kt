package com.example.planner.domain.repository

import com.example.planner.domain.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}