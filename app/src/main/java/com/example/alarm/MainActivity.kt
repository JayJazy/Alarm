package com.example.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : AppCompatActivity() {

    private lateinit var alarmManager : AlarmManager
    private lateinit var pendingIntent : PendingIntent
    private lateinit var calendar : Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        if(!alarmManager.canScheduleExactAlarms())
        {
            Toast.makeText(this,"알람 권한 설정", Toast.LENGTH_LONG).show()
            startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        }
    }


    override fun onResume() {
        super.onResume()

        val current = findViewById<TimePicker>(R.id.timePicker)
        val btn = findViewById<Button>(R.id.SetBtn)

        btn.setOnClickListener {
            val alarm = findViewById<TextView>(R.id.TimeData)
            val hour = current.hour
            val min = current.minute
            val setTime = "알람 시간\n ${current.hour}시 ${current.minute}분"
            alarm.text = setTime

            setAlarm(hour, min)
        }

    }


    private fun setAlarm(hour : Int, min : Int)
    {
        calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = min
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, null), pendingIntent)
        }
    }
}