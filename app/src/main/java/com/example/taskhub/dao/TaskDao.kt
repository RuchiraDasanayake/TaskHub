package com.example.taskhub.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskhub.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskEntity>

    @Insert
    fun insertTask(task: TaskEntity)

    @Update
    fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    fun deleteTask(taskId: Int)

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): TaskEntity
}
