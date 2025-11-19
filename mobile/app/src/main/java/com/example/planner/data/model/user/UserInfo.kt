package com.example.planner.data.model.user

import java.time.LocalDateTime
import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.parser.Table

@Entity(tableName = "user_info")
data class UserInfo(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "first_name")
    var firstName: String?,

    @ColumnInfo(name = "second_name")
    var secondName: String?,

    @ColumnInfo(name = "last_name")
    var lastName: String?,

    @ColumnInfo(name = "email")
    var email: String?,

    @ColumnInfo(name = "registration_date")
    var registrationDate: LocalDateTime?,

    @ColumnInfo(name = "profile_image")
    var profileImage: String?
)