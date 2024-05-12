package com.example.taskhub.database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskhub.dao.TaskDao
import com.example.taskhub.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
