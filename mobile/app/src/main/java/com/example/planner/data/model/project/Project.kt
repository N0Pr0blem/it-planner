package com.example.planner.data.model.project

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.planner.data.model.user.OauthUser
import java.time.LocalDateTime


@Entity(
    tableName = "project",
    foreignKeys = [
        ForeignKey(
            entity = OauthUser::class,
            parentColumns = ["id"], // Поле в родительской таблице (OauthUser)
            childColumns = ["created_user_id"], // Поле в этой таблице (Project)
            onDelete = ForeignKey.SET_NULL // Например, если пользователь удален, обнулить ID создателя
        )
    ]
)
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "creation_date")
    var creationDate: LocalDateTime?, // Понадобится TypeConverter


    @ColumnInfo(name = "created_user_id", index = true)
    var createdUserId: Long?
)
