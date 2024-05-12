package com.example.taskhub.activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskhub.dao.TaskDao
import com.example.taskhub.MyApplication
import com.example.taskhub.adapter.TasksAdapter
import com.example.taskhub.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskDao: TaskDao
    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize TaskDao
        taskDao = MyApplication.database.taskDao()

        // Initialize RecyclerView and Adapter
        tasksAdapter = TasksAdapter(mutableListOf(), taskDao)
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = tasksAdapter

        // Set OnClickListener for the Add button
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for the Sort button
        binding.sortButton.setOnClickListener {
            // Perform sorting asynchronously using coroutines
            sortTasks()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh tasks asynchronously
        refreshTasks()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun refreshTasks() {
        // Fetch tasks asynchronously using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val allTasks = withContext(Dispatchers.IO) {
                taskDao.getAllTasks()
            }
            tasksAdapter.clearTasks() // Clear existing tasks
            tasksAdapter.addTasks(allTasks) // Add new tasks
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun sortTasks() {
        // Sort tasks asynchronously using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val sortedTasks = withContext(Dispatchers.IO) {
                taskDao.getAllTasks().sortedBy { it.priorityLevel }
            }
            tasksAdapter.refreshTasks(sortedTasks)
        }
    }
}
