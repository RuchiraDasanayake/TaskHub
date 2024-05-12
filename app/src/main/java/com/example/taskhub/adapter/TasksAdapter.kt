package com.example.taskhub.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.taskhub.activity.UpdateTaskActivity
import com.example.taskhub.dao.TaskDao
import com.example.taskhub.entity.TaskEntity
import com.example.taskhub.R
import kotlinx.coroutines.*

class TasksAdapter(private var tasks: MutableList<TaskEntity>, private val taskDao: TaskDao) :
    RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
        val dueDateTextView: TextView = itemView.findViewById(R.id.dueDateTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.contentTextView.text = task.content
        holder.priorityTextView.text = task.priorityLevel.toString()
        holder.dueDateTextView.text = task.dueDate

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateTaskActivity::class.java).apply {
                putExtra("id", task.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                taskDao.deleteTask(task.id)
                // Remove the deleted task from the list
                tasks.removeAll { it.id == task.id }
                // Notify adapter of item removal on the main thread
                withContext(Dispatchers.Main) {
                    notifyItemRemoved(holder.adapterPosition)
                    // Show a toast message
                    Toast.makeText(holder.itemView.context, "Task deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TasksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    // Function to refresh the task list
    @SuppressLint("NotifyDataSetChanged")
    fun refreshTasks(newTasks: List<TaskEntity>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearTasks() {
        tasks.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addTasks(allTasks: List<TaskEntity>) {
        tasks.addAll(allTasks)
        notifyDataSetChanged()
    }
}
