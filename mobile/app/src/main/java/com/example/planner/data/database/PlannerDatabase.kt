package com.example.planner.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.planner.data.dao.OauthUserDao
import com.example.planner.data.dao.UserInfoDao
import com.example.planner.data.dao.ProjectDao
import com.example.planner.data.dao.EmployeeDao
import com.example.planner.data.dao.TaskInfoDao
import com.example.planner.data.dao.TaskDetailsDao
import com.example.planner.data.dao.TaskFileDao
import com.example.planner.data.dao.ProjectRepoDao
import com.example.planner.data.dao.ProjectRepoFileDao
import com.example.planner.data.dao.TrackingDao
import com.example.planner.data.model.converters.DateConverters
import com.example.planner.data.model.converters.EnumTypeConverters
import com.example.planner.data.model.project.Employee
import com.example.planner.data.model.project.Project
import com.example.planner.data.model.repo.ProjectRepo
import com.example.planner.data.model.repo.ProjectRepoFile
import com.example.planner.data.model.task.TaskDetails
import com.example.planner.data.model.task.TaskFile
import com.example.planner.data.model.task.TaskInfo
import com.example.planner.data.model.tracking.Trekking
import com.example.planner.data.model.user.OauthUser
import com.example.planner.data.model.user.UserInfo

@Database(
    entities = [
        OauthUser::class,
        UserInfo::class,
        Project::class,
        Employee::class,
        TaskDetails::class,
        TaskInfo::class,
        TaskFile::class,
        ProjectRepo::class,
        ProjectRepoFile::class,
        Trekking::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    EnumTypeConverters::class,
    DateConverters::class
)
abstract class PlannerDatabase : RoomDatabase() {
    abstract fun oauthUserDao(): OauthUserDao
    abstract fun userInfoDao(): UserInfoDao
    abstract fun projectDao(): ProjectDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun taskInfoDao(): TaskInfoDao
    abstract fun taskDetailsDao(): TaskDetailsDao
    abstract fun taskFileDao(): TaskFileDao
    abstract fun projectRepoDao(): ProjectRepoDao
    abstract fun projectRepoFileDao(): ProjectRepoFileDao
    abstract fun trackingDao(): TrackingDao

    companion object {
        @Volatile
        private var INSTANCE: PlannerDatabase? = null

        fun getDatabase(context: Context): PlannerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlannerDatabase::class.java,
                    "planner_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

