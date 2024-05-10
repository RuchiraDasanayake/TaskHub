package com.example.taskhub

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskhub.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TasksDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDBHelper(this)

        binding.saveBtn.setOnClickListener {
            val title = binding.editTaskTitle.text.toString()
            val content = binding.editTaskDescription.text.toString()
            val task = Task(0, title, content)
            db.insertTask(task)
            finish()
            Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()
        }
    }
}