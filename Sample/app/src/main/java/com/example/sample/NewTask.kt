package com.example.sample

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class NewTask : AppCompatActivity() {

    private var taskPosition: Int = -1
    private lateinit var taskManager: TaskManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        taskManager = TaskManager(this)

        val editTextTitle: EditText = findViewById(R.id.editTextTaskTitle)
        val editTextDescription: EditText = findViewById(R.id.editTextDescription)
        val editTextDueDate: EditText = findViewById(R.id.editTextDueDate)
        val radioGroupPriority: RadioGroup = findViewById(R.id.radioGroupPriority)
        val buttonSaveTask: Button = findViewById(R.id.buttonSaveTask)

        // Set a click listener to pick the date using date picker without typing in edit text
        editTextDueDate.setOnClickListener {
            showDatePickerDialog(editTextDueDate)
        }

        // Check the activity exist or not for updating purpose
        if (intent.hasExtra("position")) {
            taskPosition = intent.getIntExtra("position", -1)
            editTextTitle.setText(intent.getStringExtra("title"))
            editTextDescription.setText(intent.getStringExtra("description"))
            editTextDueDate.setText(intent.getStringExtra("dueDate"))

            when (intent.getStringExtra("priority")) {
                "Low" -> radioGroupPriority.check(R.id.radioLow)
                "Medium" -> radioGroupPriority.check(R.id.radioMedium)
                "High" -> radioGroupPriority.check(R.id.radioHigh)
            }
        }

        buttonSaveTask.setOnClickListener {
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()
            val dueDate = editTextDueDate.text.toString()
            val priority = when (radioGroupPriority.checkedRadioButtonId) {
                R.id.radioLow -> "Low"
                R.id.radioMedium -> "Medium"
                R.id.radioHigh -> "High"
                else -> "Medium"
            }

            val task = Task(title, description, dueDate, priority)

            if (taskPosition >= 0) {
                // calling updatinf task
                taskManager.updateTask(taskPosition, task)
            } else {
                // add a new task if no task
                val tasks = taskManager.getTasks()
                tasks.add(task)
                taskManager.saveTasks(tasks)
            }

            Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a date
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // format the selected date and set it to the edit text to presenting in field
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            editText.setText(formattedDate)
        }, year, month, day)

        // Show the date
        datePickerDialog.show()
    }
}
