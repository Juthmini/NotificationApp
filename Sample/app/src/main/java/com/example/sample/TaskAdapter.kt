package com.example.sample

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val taskManager: TaskManager
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTaskTitle: TextView = itemView.findViewById(R.id.textViewTaskTitle)
        val textViewPriority: TextView = itemView.findViewById(R.id.textViewPriority)
        val buttonEdit: ImageView = itemView.findViewById(R.id.imageView4)
        val buttonDelete: ImageView = itemView.findViewById(R.id.imageView5)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.textViewTaskTitle.text = task.title
        holder.textViewPriority.text = task.priority

        // Edit button function
        holder.buttonEdit.setOnClickListener {
            // Create an intent to the newtask activity to edit the task
            val context = holder.itemView.context
            val intent = Intent(context, NewTask::class.java).apply {
                putExtra("position", position)
                putExtra("title", task.title)
                putExtra("description", task.description)
                putExtra("dueDate", task.dueDate)
                putExtra("priority", task.priority)
            }
            context.startActivity(intent)
        }

        // Delete button function
        holder.buttonDelete.setOnClickListener {
            taskManager.deleteTask(position)
            tasks.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, tasks.size)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    // Update the list
    fun updateTasks(newTasks: MutableList<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
