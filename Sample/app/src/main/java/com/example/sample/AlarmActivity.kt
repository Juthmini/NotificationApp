package com.example.sample

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AlarmActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var setAlarmButton: Button
    private lateinit var cancelAlarmButton: Button
    private lateinit var editAlarmButton: Button
    private lateinit var alarmStatusTextView: TextView

    private var alarmManager: AlarmManager? = null
    private var alarmTimeInMillis: Long = 0L

    companion object {
        const val CHANNEL_ID = "ALARM_CHANNEL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        timePicker = findViewById(R.id.timePicker)
        setAlarmButton = findViewById(R.id.setAlarmButton)
        alarmStatusTextView = findViewById(R.id.alarmStatusTextView)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        createNotificationChannel()  // Create notification channel

        // Set alarm button click listener
        setAlarmButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            calendar.set(Calendar.SECOND, 0)

            // Set the alarm time
            alarmTimeInMillis = calendar.timeInMillis

            // Check if the device is running android version and alarm permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager?.canScheduleExactAlarms() == true) {
                    setAlarm(alarmTimeInMillis)
                } else {
                    // Requesting the permission
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    startActivity(intent)
                }
            } else {
                // Directly set the alarm
                setAlarm(alarmTimeInMillis)
            }

            displayAlarmStatus(alarmTimeInMillis)
        }

    }

    private fun setAlarm(timeInMillis: Long) {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set an exact alarm
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    @SuppressLint("SetTextI18n")
    private fun cancelAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager?.cancel(pendingIntent)
            pendingIntent.cancel()
            alarmTimeInMillis = 0L
            alarmStatusTextView.text = "Alarm is cancelled."
        } else {
            alarmStatusTextView.text = "No alarm set to cancel."
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayAlarmStatus(timeInMillis: Long) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val alarmTime = sdf.format(Date(timeInMillis))
        alarmStatusTextView.text = "Alarm set for: $alarmTime"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Notification"
            val descriptionText = "Alarm Notification Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
