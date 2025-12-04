package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.repo.ProjectRepo
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectRepoDao {
    @Query("SELECT * FROM project_repository")
    fun getAll(): Flow<List<ProjectRepo>>

    @Query("SELECT * FROM project_repository WHERE id = :id")
    suspend fun getById(id: Long): ProjectRepo?

    @Query("SELECT * FROM project_repository WHERE project_id = :projectId")
    fun getByProjectId(projectId: Long): Flow<List<ProjectRepo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(projectRepo: ProjectRepo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projectRepos: List<ProjectRepo>)

    @Update
    suspend fun update(projectRepo: ProjectRepo)

    @Delete
    suspend fun delete(projectRepo: ProjectRepo)

    @Query("DELETE FROM project_repository WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM project_repository WHERE project_id = :projectId")
    suspend fun deleteByProjectId(projectId: Long)

    @Query("DELETE FROM project_repository")
    suspend fun deleteAll()
}



