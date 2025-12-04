package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.project.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun getAll(): Flow<List<Project>>

    @Query("SELECT * FROM project WHERE id = :id")
    suspend fun getById(id: Long): Project?

    @Query("SELECT * FROM project WHERE created_user_id = :userId")
    fun getByUserId(userId: Long): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: Project): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projects: List<Project>)

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)

    @Query("DELETE FROM project WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM project")
    suspend fun deleteAll()
}



