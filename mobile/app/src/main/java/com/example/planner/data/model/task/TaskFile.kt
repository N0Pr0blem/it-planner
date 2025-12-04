package com.example.planner.data.model.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.planner.data.model.repo.FileType

@Entity(
    tableName = "task_file",
    foreignKeys = [
        ForeignKey(
            entity = TaskInfo::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskFile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "task_id", index = true)
    val taskId: Long?,

    @ColumnInfo(name = "type")
    val type: FileType,

    @ColumnInfo(name = "name")
    val name: String?
)