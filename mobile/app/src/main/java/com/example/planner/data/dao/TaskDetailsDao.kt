package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.task.TaskDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDetailsDao {
    @Query("SELECT * FROM task_details")
    fun getAll(): Flow<List<TaskDetails>>

    @Query("SELECT * FROM task_details WHERE id = :id")
    suspend fun getById(id: Long): TaskDetails?

    @Query("SELECT * FROM task_details WHERE from_user_id = :userId")
    fun getByFromUserId(userId: Long): Flow<List<TaskDetails>>

    @Query("SELECT * FROM task_details WHERE to_user_id = :userId")
    fun getByToUserId(userId: Long): Flow<List<TaskDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskDetails: TaskDetails): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(taskDetailsList: List<TaskDetails>)

    @Update
    suspend fun update(taskDetails: TaskDetails)

    @Delete
    suspend fun delete(taskDetails: TaskDetails)

    @Query("DELETE FROM task_details WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM task_details")
    suspend fun deleteAll()
}

