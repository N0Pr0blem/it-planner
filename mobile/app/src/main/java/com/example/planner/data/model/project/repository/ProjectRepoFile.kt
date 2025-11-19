package com.example.planner.data.model.project.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "project_repository_file",
    foreignKeys = [
        ForeignKey(
            entity = ProjectRepo::class,
            parentColumns = ["id"],
            childColumns = ["project_repository_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProjectRepoFile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // 1. Вместо объекта ProjectRepo храним только его ID
    @ColumnInfo(name = "project_repository_id", index = true)
    var projectRepoId: Long,

    // 2. Храним Enum как строку. Потребуется TypeConverter.
    @ColumnInfo(name = "type")
    var type: FileType,

    @ColumnInfo(name = "name")
    var name: String?
)

