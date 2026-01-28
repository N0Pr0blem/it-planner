package com.example.planner.ui.navigation

import android.net.Uri

sealed class NavRoutes(val route: String) {
    object Login : NavRoutes("login")
    object Register : NavRoutes("register")
    object Verify : NavRoutes("verify/{username}") {
        fun createRoute(username: String) = "verify/${Uri.encode(username)}"
    }
    object Projects : NavRoutes("projects")
    object CreateProject : NavRoutes("create_project")
    object ProjectDetails : NavRoutes("project_details/{projectId}") {
        fun createRoute(projectId: Long) = "project_details/$projectId"
    }
    object CreateTask : NavRoutes("create_task/{projectId}") {
        fun createRoute(projectId: Long) = "create_task/$projectId"
    }
    object TaskDetails : NavRoutes("task_details/{projectId}/{taskId}") {
        fun createRoute(projectId: Long, taskId: Long) = "task_details/$projectId/$taskId"
    }
    object TimeAndAssignees : NavRoutes("time_assignees/{projectId}/{taskId}") {
        fun createRoute(projectId: Long, taskId: Long) = "time_assignees/$projectId/$taskId"
    }
    object InviteMember : NavRoutes("invite_member/{projectId}") {
        fun createRoute(projectId: Long) = "invite_member/$projectId"
    }
    object PersonalAccount : NavRoutes("personal_account")
    object ProjectRepo : NavRoutes("project_repo/{projectId}") {
        fun createRoute(projectId: Long) = "project_repo/$projectId"
    }
    object TaskUserInfo : NavRoutes("task_user_info/{projectId}/{employeeId}") {
        fun createRoute(projectId: Long, employeeId: Long) = "task_user_info/$projectId/$employeeId"
    }
}
