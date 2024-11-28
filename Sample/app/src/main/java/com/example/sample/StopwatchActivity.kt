package com.example.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StopwatchActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var lapContainer: LinearLayout
    private var handler: Handler = Handler()
    private var startTime: Long = 0L
    private var timeInMilliseconds: Long = 0L
    private var updatedTime: Long = 0L
    private var timeSwapBuff: Long = 0L
    private var laps: MutableList<String> = mutableListOf()

    private val updateTimerThread = object : Runnable {
        @SuppressLint("DefaultLocale")
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updatedTime = timeSwapBuff + timeInMilliseconds
            val secs = (updatedTime / 1000).toInt() % 60
            val mins = (updatedTime / 60000).toInt() % 60
            val hours = (updatedTime / 3600000).toInt()
            timerTextView.text = String.format("%02d:%02d:%02d", hours, mins, secs)
            handler.postDelayed(this, 0)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)

        timerTextView = findViewById(R.id.timer)
        lapContainer = findViewById(R.id.lapContainer)

        val startButton: Button = findViewById(R.id.startButton)
        val stopButton: Button = findViewById(R.id.stopButton)
        val resetButton: Button = findViewById(R.id.resetButton)
        val lapButton: Button = findViewById(R.id.lapButton)

        startButton.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(updateTimerThread, 0)
        }

        stopButton.setOnClickListener {
            timeSwapBuff += timeInMilliseconds
            handler.removeCallbacks(updateTimerThread)
        }

        resetButton.setOnClickListener {
            startTime = 0L
            timeInMilliseconds = 0L
            timeSwapBuff = 0L
            updatedTime = 0L
            timerTextView.text = "00:00:00"
            lapContainer.removeAllViews()
        }

        lapButton.setOnClickListener {
            val lapTime = timerTextView.text.toString()
            laps.add(lapTime)
            displayLapTimes()
        }

        val searchButton: Button = findViewById(R.id.remindersButton)

        searchButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayLapTimes() {
        lapContainer.removeAllViews()
        for (i in laps.indices) {
            val lapText = TextView(this).apply {
                text = "Lap ${i + 1} - ${laps[i]}"
                setTextColor(resources.getColor(R.color.white))
            }
            lapContainer.addView(lapText)
        }
    }
}
