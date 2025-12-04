package com.example.planner.data.model.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.planner.data.model.project.Employee

@Entity(
    tableName = "task_details",
    foreignKeys = [
        ForeignKey(
            entity = Employee::class,
            parentColumns = ["id"],
            childColumns = ["from_user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Employee::class,
            parentColumns = ["id"],
            childColumns = ["to_user_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class TaskDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "from_user_id", index = true)
    val fromUserId: Long,

    @ColumnInfo(name = "to_user_id", index = true)
    val toUserId: Long?,

    @ColumnInfo(name = "description_file")
    val descriptionFile: String?
)