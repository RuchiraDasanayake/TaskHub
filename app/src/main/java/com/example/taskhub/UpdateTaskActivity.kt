package com.example.taskhub

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskhub.databinding.ActivityUpdateTaskBinding

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var db: TasksDBHelper
    private lateinit var binding: ActivityUpdateTaskBinding
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDBHelper(this)
        taskId = intent.getIntExtra("id", -1)
        if (taskId == -1){
            finish()
            return
        }

        val task = db.getTaskById(taskId)
        binding.updateEditTaskTitle.setText(task.title)
        binding.updateEditTaskDescription.setText(task.content)
        binding.updateEditTaskDueDate.setText(task.dueDate)

        binding.updateSaveBtn.setOnClickListener {
            val newTitle = binding.updateEditTaskTitle.text.toString()
            val newContent = binding.updateEditTaskDescription.text.toString()

            val selectedPriorityRadioButtonId = binding.updateEditTaskPriority.checkedRadioButtonId

            val newPriorityLevel = when (selectedPriorityRadioButtonId) {
                R.id.priority1RadioButtonUpdate -> 1
                R.id.priority2RadioButtonUpdate -> 2
                R.id.priority3RadioButtonUpdate -> 3
                else -> 0
            }

            val newDueDate = binding.updateEditTaskDueDate.text.toString()
            val updatedTask = Task(taskId, newTitle, newContent, newPriorityLevel, newDueDate)
            db.updateTask(updatedTask)
            finish()
            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show()
        }

    }
}