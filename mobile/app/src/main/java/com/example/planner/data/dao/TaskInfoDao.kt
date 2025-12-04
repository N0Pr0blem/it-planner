package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.task.TaskInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskInfoDao {
    @Query("SELECT * FROM task_info")
    fun getAll(): Flow<List<TaskInfo>>

    @Query("SELECT * FROM task_info WHERE id = :id")
    suspend fun getById(id: Long): TaskInfo?

    @Query("SELECT * FROM task_info WHERE project_id = :projectId")
    fun getByProjectId(projectId: Long): Flow<List<TaskInfo>>

    @Query("SELECT * FROM task_info WHERE task_details_id = :taskDetailsId")
    suspend fun getByTaskDetailsId(taskDetailsId: Long): TaskInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskInfo: TaskInfo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(taskInfos: List<TaskInfo>)

    @Update
    suspend fun update(taskInfo: TaskInfo)

    @Delete
    suspend fun delete(taskInfo: TaskInfo)

    @Query("DELETE FROM task_info WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM task_info WHERE project_id = :projectId")
    suspend fun deleteByProjectId(projectId: Long)

    @Query("DELETE FROM task_info")
    suspend fun deleteAll()
}

