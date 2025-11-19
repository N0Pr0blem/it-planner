package com.example.planner.data.model.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.planner.data.model.project.Project
import java.time.LocalDateTime


@Entity(
    tableName = "task_info",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // 1. Храним только ID проекта, а не весь объект.
    @ColumnInfo(name = "project_id", index = true)
    val projectId: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false,

    // 2. Храним Enum как строку. Понадобится TypeConverter.
    @ColumnInfo(name = "complexity")
    var complexity: TaskComplexity = TaskComplexity.MEDIUM,

    // 2. Храним Enum как строку. Понадобится TypeConverter.
    @ColumnInfo(name = "urgency")
    var urgency: TaskUrgency = TaskUrgency.URGENT,

    @ColumnInfo(name = "creation_date")
    var creationDate: LocalDateTime?
)

