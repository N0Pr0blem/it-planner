package com.example.planner.data.model.project.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.planner.data.model.project.Project

@Entity(
    tableName = "project_repository",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProjectRepo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "project_id", index = true)
    val projectId: Long,

    @ColumnInfo(name = "path")
    val path: String
)
