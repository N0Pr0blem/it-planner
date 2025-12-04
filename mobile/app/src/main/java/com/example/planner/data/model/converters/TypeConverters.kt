package com.example.planner.data.model.converters

import androidx.room.TypeConverter
import com.example.planner.data.model.task.TaskComplexity
import com.example.planner.data.model.task.TaskStatus
import com.example.planner.data.model.task.TaskUrgency
import com.example.planner.data.model.repo.FileType
import com.example.planner.data.model.user.OauthRole
import com.example.planner.data.model.user.ProjectRole

class TypeConverters {

    // TaskComplexity
    @TypeConverter
    fun fromTaskComplexity(complexity: TaskComplexity?): String? = complexity?.name

    @TypeConverter
    fun toTaskComplexity(name: String?): TaskComplexity? = name?.let { TaskComplexity.valueOf(it) }

    // TaskUrgency
    @TypeConverter
    fun fromTaskUrgency(urgency: TaskUrgency?): String? = urgency?.name

    @TypeConverter
    fun toTaskUrgency(name: String?): TaskUrgency? = name?.let { TaskUrgency.valueOf(it) }

    // TaskStatus
    @TypeConverter
    fun fromTaskStatus(status: TaskStatus?): String? = status?.name

    @TypeConverter
    fun toTaskStatus(name: String?): TaskStatus? = name?.let { TaskStatus.valueOf(it) }

    // FileType
    @TypeConverter
    fun fromFileType(type: FileType?): String? = type?.name

    @TypeConverter
    fun toFileType(name: String?): FileType? = name?.let { FileType.valueOf(it) }
    // OauthRole
    @TypeConverter
    fun fromOauthRole(role: OauthRole?): String? = role?.name

    @TypeConverter
    fun toOauthRole(name: String?): OauthRole? = name?.let { OauthRole.valueOf(it) }

    // ProjectRole
    @TypeConverter
    fun fromProjectRole(role: ProjectRole?): String? = role?.name

    @TypeConverter
    fun toProjectRole(name: String?): ProjectRole? = name?.let { ProjectRole.valueOf(it) }
}