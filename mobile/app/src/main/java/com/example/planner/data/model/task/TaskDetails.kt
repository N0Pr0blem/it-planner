package com.example.planner.data.model.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.planner.data.model.user.OauthUser

@Entity(
    tableName = "task_details",
    // 1. Определяем внешние ключи для связи с таблицей 'oauth_user'.
    // Room будет проверять, что `from_user_id` и `to_user_id` существуют в `oauth_user`.
    foreignKeys = [
        ForeignKey(
            entity = OauthUser::class,
            parentColumns = ["id"],
            childColumns = ["from_user_id"],
            onDelete = ForeignKey.CASCADE // Пример: удалить детали, если пользователь удален
        ),
        ForeignKey(
            entity = OauthUser::class,
            parentColumns = ["id"],
            childColumns = ["to_user_id"],
            onDelete = ForeignKey.SET_NULL // Пример: обнулить поле, если пользователь удален
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
