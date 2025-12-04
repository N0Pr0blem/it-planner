package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.user.UserInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {
    @Query("SELECT * FROM user_info")
    fun getAll(): Flow<List<UserInfo>>

    @Query("SELECT * FROM user_info WHERE id = :id")
    suspend fun getById(id: String): UserInfo?

    @Query("SELECT * FROM user_info WHERE email = :email")
    suspend fun getByEmail(email: String): UserInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userInfo: UserInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userInfos: List<UserInfo>)

    @Update
    suspend fun update(userInfo: UserInfo)

    @Delete
    suspend fun delete(userInfo: UserInfo)

    @Query("DELETE FROM user_info WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM user_info")
    suspend fun deleteAll()
}

