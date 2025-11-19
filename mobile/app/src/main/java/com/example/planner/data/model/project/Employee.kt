package com.example.planner.data.model.project

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.planner.data.model.user.OauthUser


@Entity(
    tableName = "project_employee",
    primaryKeys = ["user_id", "project_id"],
    foreignKeys = [
        ForeignKey(
            entity = OauthUser::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE // Если пользователь удален, удалить и его связь с проектом
        ),
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE // Если проект удален, удалить и всех его участников
        )
    ]
)
data class Employee(
    @ColumnInfo(name = "user_id", index = true)
    val userId: Long,

    @ColumnInfo(name = "project_id", index = true)
    val projectId: Long
)
