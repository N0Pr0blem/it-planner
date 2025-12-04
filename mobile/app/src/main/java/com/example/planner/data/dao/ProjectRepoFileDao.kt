package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.repo.ProjectRepoFile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectRepoFileDao {
    @Query("SELECT * FROM project_repository_file")
    fun getAll(): Flow<List<ProjectRepoFile>>

    @Query("SELECT * FROM project_repository_file WHERE id = :id")
    suspend fun getById(id: Long): ProjectRepoFile?

    @Query("SELECT * FROM project_repository_file WHERE project_repository_id = :projectRepoId")
    fun getByProjectRepoId(projectRepoId: Long): Flow<List<ProjectRepoFile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(projectRepoFile: ProjectRepoFile): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projectRepoFiles: List<ProjectRepoFile>)

    @Update
    suspend fun update(projectRepoFile: ProjectRepoFile)

    @Delete
    suspend fun delete(projectRepoFile: ProjectRepoFile)

    @Query("DELETE FROM project_repository_file WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM project_repository_file WHERE project_repository_id = :projectRepoId")
    suspend fun deleteByProjectRepoId(projectRepoId: Long)

    @Query("DELETE FROM project_repository_file")
    suspend fun deleteAll()
}

