package com.example.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.os.Build
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AlarmReceiver : BroadcastReceiver() {

    private var tts : TextToSpeech? = null

    override fun onReceive(context: Context, intent: Intent) {
        alarmTTS(context)
    }

    private fun alarmTTS(context: Context)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH시 mm분")
            val currentTime = current.format(formatter)

            Toast.makeText(context, "현재 시간 : $currentTime", Toast.LENGTH_LONG).show()

            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    audioManager.setStreamVolume(
                        AudioManager.STREAM_NOTIFICATION, (audioManager.getStreamMaxVolume(
                            AudioManager.STREAM_NOTIFICATION) * 3 / 15),0)
                    tts?.setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build())
                    tts?.speak("현재 시간은 $currentTime 입니다", TextToSpeech.QUEUE_FLUSH, null, null)
                    tts?.playSilentUtterance(1000, TextToSpeech.QUEUE_ADD, null)
                }
            })

        }
    }


}