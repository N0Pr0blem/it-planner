package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.task.TaskFile
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskFileDao {
    @Query("SELECT * FROM task_file")
    fun getAll(): Flow<List<TaskFile>>

    @Query("SELECT * FROM task_file WHERE id = :id")
    suspend fun getById(id: Long): TaskFile?

    @Query("SELECT * FROM task_file WHERE task_id = :taskId")
    fun getByTaskId(taskId: Long): Flow<List<TaskFile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskFile: TaskFile): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(taskFiles: List<TaskFile>)

    @Update
    suspend fun update(taskFile: TaskFile)

    @Delete
    suspend fun delete(taskFile: TaskFile)

    @Query("DELETE FROM task_file WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM task_file WHERE task_id = :taskId")
    suspend fun deleteByTaskId(taskId: Long)

    @Query("DELETE FROM task_file")
    suspend fun deleteAll()
}



