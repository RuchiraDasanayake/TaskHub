package com.example.taskhub.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskhub.dao.TaskDao
import com.example.taskhub.entity.TaskEntity
import com.example.taskhub.MyApplication
import com.example.taskhub.R
import com.example.taskhub.databinding.ActivityUpdateTaskBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var taskDao: TaskDao
    private lateinit var binding: ActivityUpdateTaskBinding
    private var taskId: Int = -1
    private lateinit var selectedDueDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize TaskDao
        taskDao = MyApplication.database.taskDao()

        taskId = intent.getIntExtra("id", -1)
        if (taskId == -1) {
            finish()
            return
        }

        // Set OnClickListener for the button to show the date picker dialog
        binding.updateEditTaskDueDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.updateSaveBtn.setOnClickListener {
            updateTask()
        }

        loadTask()
    }

    // Function to show the date picker dialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the selectedDueDate variable with the selected date
                selectedDueDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                // Set the selected date in the EditText
                binding.updateEditTaskDueDate.setText(selectedDueDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    // Function to load task details
    @OptIn(DelicateCoroutinesApi::class)
    private fun loadTask() {
        GlobalScope.launch(Dispatchers.Main) {
            val task = withContext(Dispatchers.IO) {
                taskDao.getTaskById(taskId)
            }
            task.let {
                binding.updateEditTaskTitle.setText(it.title)
                binding.updateEditTaskDescription.setText(it.content)
                binding.updateEditTaskDueDate.setText(it.dueDate)
                selectedDueDate = it.dueDate
            }
        }
    }

    // Function to update task in the database
    @OptIn(DelicateCoroutinesApi::class)
    private fun updateTask() {
        val newTitle = binding.updateEditTaskTitle.text.toString()
        val newContent = binding.updateEditTaskDescription.text.toString()

        val selectedPriorityRadioButtonId = binding.updateEditTaskPriority.checkedRadioButtonId
        val newPriorityLevel = when (selectedPriorityRadioButtonId) {
            R.id.priority1RadioButtonUpdate -> 1
            R.id.priority2RadioButtonUpdate -> 2
            R.id.priority3RadioButtonUpdate -> 3
            else -> 0
        }

        // Use selectedDueDate as the due date for the updated task
        val updatedTask = TaskEntity(taskId, newTitle, newContent, newPriorityLevel, selectedDueDate)

        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                taskDao.updateTask(updatedTask)
            }
            finish()
            Toast.makeText(this@UpdateTaskActivity, "Task updated", Toast.LENGTH_SHORT).show()
        }
    }
}
