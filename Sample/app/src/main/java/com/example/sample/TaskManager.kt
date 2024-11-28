package com.example.sample

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("TaskPreferences", Context.MODE_PRIVATE)

    private val gson = Gson()

    // Save task list to shared preferences
    fun saveTasks(taskList: List<Task>) {
        val json = gson.toJson(taskList)
        sharedPreferences.edit().putString("tasks", json).apply()
    }

    // Get task list from shared preferences
    fun getTasks(): MutableList<Task> {
        val json = sharedPreferences.getString("tasks", null)
        return if (json != null) {
            val type = object : TypeToken<List<Task>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    // Update a task
    fun updateTask(position: Int, newTask: Task) {
        val tasks = getTasks()
        tasks[position] = newTask
        saveTasks(tasks)
    }

    // Delete a task
    fun deleteTask(position: Int) {
        val tasks = getTasks()
        tasks.removeAt(position)
        saveTasks(tasks)
    }
}
