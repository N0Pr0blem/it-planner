package com.example.planner.data.model.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DateConverters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? = date?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(epoch: Long?): LocalDate? = epoch?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate() }
}