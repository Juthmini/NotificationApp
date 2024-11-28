package com.example.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class TaskListActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskManager: TaskManager

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        taskManager = TaskManager(this)

        val recyclerViewTasks = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val buttonAddTask = findViewById<Button>(R.id.buttonAddTask)

        // Setup RecyclerView with a linera layout and the adapter
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // Pass the taskManager and tasks list to the TaskAdapter
        taskAdapter = TaskAdapter(taskManager.getTasks(), taskManager)
        recyclerViewTasks.adapter = taskAdapter

        // Add task onclick listener
        buttonAddTask.setOnClickListener {
            val intent = Intent(this, NewTask::class.java)
            startActivity(intent)
        }

        // bottom navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_tasks -> {
                    Toast.makeText(this, "Tasks Selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_stopwatch -> {
                    val intent = Intent(this, StopwatchActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_reminders -> {
                    Toast.makeText(this, "Reminders Selected", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AlarmActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        // Refresh task list when coming back
        taskAdapter.updateTasks(taskManager.getTasks())
        taskAdapter.notifyDataSetChanged()
    }
}
