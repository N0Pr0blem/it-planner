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
        ),
        ForeignKey(
            entity = TaskDetails::class,
            parentColumns = ["id"],
            childColumns = ["task_details_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class TaskInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "project_id", index = true)
    val projectId: Long,

    @ColumnInfo(name = "task_details_id", index = true)
    val taskDetailsId: Long? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false,

    @ColumnInfo(name = "complexity")
    var complexity: TaskComplexity? = null,

    @ColumnInfo(name = "urgency")
    var urgency: TaskUrgency? = null,

    @ColumnInfo(name = "status")
    var status: TaskStatus? = null,

    @ColumnInfo(name = "creation_date")
    var creationDate: LocalDateTime? = null
)