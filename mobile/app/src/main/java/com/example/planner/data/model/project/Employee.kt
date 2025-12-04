package com.example.planner.data.model.project

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.planner.data.model.user.OauthUser
import com.example.planner.data.model.user.ProjectRole

@Entity(
    tableName = "project_employee",
    foreignKeys = [
        ForeignKey(
            entity = OauthUser::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Employee(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo(name = "user_id", index = true)
    val userId: Long,

    @ColumnInfo(name = "project_id", index = true)
    val projectId: Long,

    @ColumnInfo(name = "project_role")
    var projectRole: ProjectRole
)