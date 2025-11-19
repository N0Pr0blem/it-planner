package com.example.planner.data.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "oauth_user")
data class OauthUser(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "username")
    var username: String?,

    @ColumnInfo(name = "password")
    var password: String?,

    @ColumnInfo(name = "enabled")
    var enabled: Boolean = false,

    @ColumnInfo(name = "verification_code")
    var verificationCode: String?,

    @ColumnInfo(name = "oauth_role")
    var role: OauthRole = OauthRole.USER
)
