package com.example.planner.di

import com.example.planner.domain.model.Project
import com.example.planner.domain.model.Task
import com.example.planner.domain.model.ProjectMember

/**
 * Simple in-memory cache module for better performance
 * This can be replaced with a more sophisticated caching solution later
 */
object CacheModule {
    
    // Projects cache
    private var projectsCache: List<Project>? = null
    private var projectsCacheTimestamp: Long = 0
    private const val PROJECTS_CACHE_TTL = 5 * 60 * 1000 // 5 minutes
    
    // Tasks cache
    private val tasksCache = mutableMapOf<Long, List<Task>>()
    private val tasksCacheTimestamp = mutableMapOf<Long, Long>()
    private const val TASKS_CACHE_TTL = 5 * 60 * 1000 // 5 minutes
    
    // Project members cache
    private val usersCache = mutableMapOf<Long, List<ProjectMember>>()
    private val usersCacheTimestamp = mutableMapOf<Long, Long>()
    private const val USERS_CACHE_TTL = 5 * 60 * 1000 // 5 minutes
    
    // Projects cache operations
    fun getCachedProjects(): List<Project>? {
        return if (projectsCache != null && 
            System.currentTimeMillis() - projectsCacheTimestamp < PROJECTS_CACHE_TTL) {
            projectsCache
        } else {
            null
        }
    }
    
    fun cacheProjects(projects: List<Project>) {
        projectsCache = projects
        projectsCacheTimestamp = System.currentTimeMillis()
    }
    
    fun clearProjectsCache() {
        projectsCache = null
        projectsCacheTimestamp = 0
    }
    
    // Tasks cache operations
    fun getCachedTasks(projectId: Long): List<Task>? {
        return if (tasksCache[projectId] != null && 
            System.currentTimeMillis() - (tasksCacheTimestamp[projectId] ?: 0) < TASKS_CACHE_TTL) {
            tasksCache[projectId]
        } else {
            null
        }
    }
    
    fun cacheTasks(projectId: Long, tasks: List<Task>) {
        tasksCache[projectId] = tasks
        tasksCacheTimestamp[projectId] = System.currentTimeMillis()
    }
    
    fun clearTasksCache(projectId: Long) {
        tasksCache.remove(projectId)
        tasksCacheTimestamp.remove(projectId)
    }
    
    fun clearAllTasksCache() {
        tasksCache.clear()
        tasksCacheTimestamp.clear()
    }
    
    // Users cache operations
    fun getCachedUsers(projectId: Long): List<ProjectMember>? {
        return if (usersCache[projectId] != null && 
            System.currentTimeMillis() - (usersCacheTimestamp[projectId] ?: 0) < USERS_CACHE_TTL) {
            usersCache[projectId]
        } else {
            null
        }
    }
    
    fun cacheUsers(projectId: Long, users: List<ProjectMember>) {
        usersCache[projectId] = users
        usersCacheTimestamp[projectId] = System.currentTimeMillis()
    }
    
    fun clearUsersCache(projectId: Long) {
        usersCache.remove(projectId)
        usersCacheTimestamp.remove(projectId)
    }
    
    fun clearAllUsersCache() {
        usersCache.clear()
        usersCacheTimestamp.clear()
    }
    
    // Clear all caches
    fun clearAllCaches() {
        clearProjectsCache()
        clearAllTasksCache()
        clearAllUsersCache()
    }
}
