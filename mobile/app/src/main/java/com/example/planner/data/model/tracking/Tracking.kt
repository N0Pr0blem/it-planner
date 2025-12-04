package com.example.planner.data.model.tracking

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.planner.data.model.project.Employee
import com.example.planner.data.model.task.TaskDetails
import java.time.LocalDate

@Entity(
    tableName = "task_trekking",
    foreignKeys = [
        ForeignKey(
            entity = Employee::class,
            parentColumns = ["id"],
            childColumns = ["employee_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TaskDetails::class,
            parentColumns = ["id"],
            childColumns = ["task_details_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Trekking(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "employee_id", index = true)
    val employeeId: Long,

    @ColumnInfo(name = "task_details_id", index = true)
    val taskDetailsId: Long,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "hours")
    val hours: Double
)