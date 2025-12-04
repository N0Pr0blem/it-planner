package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.user.OauthUser
import kotlinx.coroutines.flow.Flow

@Dao
interface OauthUserDao {
    @Query("SELECT * FROM oauth_user")
    fun getAll(): Flow<List<OauthUser>>

    @Query("SELECT * FROM oauth_user WHERE id = :id")
    suspend fun getById(id: Long): OauthUser?

    @Query("SELECT * FROM oauth_user WHERE username = :username")
    suspend fun getByUsername(username: String): OauthUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: OauthUser): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<OauthUser>)

    @Update
    suspend fun update(user: OauthUser)

    @Delete
    suspend fun delete(user: OauthUser)

    @Query("DELETE FROM oauth_user WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM oauth_user")
    suspend fun deleteAll()
}



