package com.example.planner.data.model.typeConverters

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planner.data.model.task.TaskComplexity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    @Provides
    fun taskComplexityToString(complexity: TaskComplexity?) = complexity?.name
    @Provides
    fun stringToTaskComplexity(name: String?) = name?.let { TaskComplexity.valueOf(it) }

    // аналогично для TaskUrgency, TaskStatus

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    fun localDateTimeToDate(date: LocalDateTime?) = date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    fun dateToLocalDateTime(epoch: Long?) = epoch?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
}