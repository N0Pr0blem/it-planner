package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.project.Employee
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM project_employee")
    fun getAll(): Flow<List<Employee>>

    @Query("SELECT * FROM project_employee WHERE id = :id")
    suspend fun getById(id: Long): Employee?

    @Query("SELECT * FROM project_employee WHERE user_id = :userId")
    fun getByUserId(userId: Long): Flow<List<Employee>>

    @Query("SELECT * FROM project_employee WHERE project_id = :projectId")
    fun getByProjectId(projectId: Long): Flow<List<Employee>>

    @Query("SELECT * FROM project_employee WHERE user_id = :userId AND project_id = :projectId")
    suspend fun getByUserIdAndProjectId(userId: Long, projectId: Long): Employee?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: Employee): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(employees: List<Employee>)

    @Update
    suspend fun update(employee: Employee)

    @Delete
    suspend fun delete(employee: Employee)

    @Query("DELETE FROM project_employee WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM project_employee WHERE project_id = :projectId")
    suspend fun deleteByProjectId(projectId: Long)

    @Query("DELETE FROM project_employee")
    suspend fun deleteAll()
}



