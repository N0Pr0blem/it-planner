package com.example.planner.data.dao

import androidx.room.*
import com.example.planner.data.model.tracking.Trekking
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingDao {
    @Query("SELECT * FROM task_trekking")
    fun getAll(): Flow<List<Trekking>>

    @Query("SELECT * FROM task_trekking WHERE id = :id")
    suspend fun getById(id: Long): Trekking?

    @Query("SELECT * FROM task_trekking WHERE employee_id = :employeeId")
    fun getByEmployeeId(employeeId: Long): Flow<List<Trekking>>

    @Query("SELECT * FROM task_trekking WHERE task_details_id = :taskDetailsId")
    fun getByTaskDetailsId(taskDetailsId: Long): Flow<List<Trekking>>

    @Query("SELECT * FROM task_trekking WHERE employee_id = :employeeId AND task_details_id = :taskDetailsId")
    fun getByEmployeeIdAndTaskDetailsId(employeeId: Long, taskDetailsId: Long): Flow<List<Trekking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tracking: Trekking): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trackings: List<Trekking>)

    @Update
    suspend fun update(tracking: Trekking)

    @Delete
    suspend fun delete(tracking: Trekking)

    @Query("DELETE FROM task_trekking WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM task_trekking WHERE employee_id = :employeeId")
    suspend fun deleteByEmployeeId(employeeId: Long)

    @Query("DELETE FROM task_trekking WHERE task_details_id = :taskDetailsId")
    suspend fun deleteByTaskDetailsId(taskDetailsId: Long)

    @Query("DELETE FROM task_trekking")
    suspend fun deleteAll()
}

