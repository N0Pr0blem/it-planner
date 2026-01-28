package com.example.planner.data.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.util.Date

class LocalDateTimeAdapter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME


    @ToJson
    fun toJson(value: LocalDateTime): String = value.format(formatter)

    @FromJson
    fun fromJson(value: String): LocalDateTime = LocalDateTime.parse(value, formatter)
}

class LocalDateAdapter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @ToJson
    fun toJson(value: LocalDate): String = value.format(formatter)

    @FromJson
    fun fromJson(value: String): LocalDate = LocalDate.parse(value, formatter)
}

class DateAdapter {
    @ToJson
    fun toJson(value: Date): String = value.toInstant().toString()

    @FromJson
    fun fromJson(value: String): Date {
        // accept either epoch millis or ISO-8601 string
        val millis = value.toLongOrNull()
        return if (millis != null) {
            Date(millis)
        } else {
            Date.from(Instant.parse(value))
        }
    }
}
