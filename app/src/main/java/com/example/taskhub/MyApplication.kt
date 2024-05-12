package com.example.taskhub
import android.app.Application
import androidx.room.Room
import com.example.taskhub.database.TaskDatabase

class MyApplication : Application() {
    companion object {
        lateinit var database: TaskDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "tasks_database"
        ).build()
    }
}
