package com.example.taskhub

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(private var tasks: List<Task>, context: Context): RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    class TasksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.contentTextView.text = task.content
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TasksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun refreshTasks(newTasks: List<Task>){
        tasks = newTasks
        notifyDataSetChanged()
    }

}