package com.example.taskhub
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskhub.databinding.ActivityAddTaskBinding
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TasksDBHelper
    private lateinit var selectedDueDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDBHelper(this)

        // Set onClickListener for the button to show the date picker dialog
        val selectDueDateBtn: Button = findViewById(R.id.selectDueDateBtn)
        selectDueDateBtn.setOnClickListener {
            showDatePickerDialog()
        }

        binding.saveBtn.setOnClickListener {
            // Get other task details like title, content, priority, etc.
            val title = binding.editTaskTitle.text.toString()
            val content = binding.editTaskDescription.text.toString()
            val priorityLevel = getSelectedPriority()

            // Create a new task object with the collected details including the due date
            val task = Task(0, title, content, priorityLevel, selectedDueDate)

            // Insert the task into the database
            db.insertTask(task)

            // Finish the activity and show a toast message
            finish()
            Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()
        }
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
                val dueDateEditText: EditText = findViewById(R.id.editTaskDueDate)
                dueDateEditText.setText(selectedDueDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    // Function to get the selected priority level
    private fun getSelectedPriority(): Int {
        val selectedPriorityRadioButtonId = binding.editTaskPriority.checkedRadioButtonId
        return when (selectedPriorityRadioButtonId) {
            R.id.priority1RadioButton -> 1
            R.id.priority2RadioButton -> 2
            R.id.priority3RadioButton -> 3
            else -> 0
        }
    }
}
