package com.example.planner.domain.repositoryImpl

import com.example.planner.domain.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}