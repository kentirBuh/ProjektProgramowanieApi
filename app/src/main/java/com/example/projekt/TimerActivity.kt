package com.example.projekt

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt.databinding.ActivityTimerBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import kotlin.math.roundToInt

class TimerActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTimerBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startStopButton.setOnClickListener(){
            startStopTimer()
        }
        binding.resetButton.setOnClickListener(){
            resetTimer()
        }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED), RECEIVER_EXPORTED)
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIMER_EXTRA, 0.0)
            binding.timeTV.text = getTimeStringFromDouble(time)

        }
    }
    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hours, min, sec)

    private fun startStopTimer() {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }


    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.timeTV.text = getTimeStringFromDouble(time)
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIMER_EXTRA, time)
        startService(serviceIntent)
        binding.startStopButton.text = "Stop"
        binding.startStopButton.icon = getDrawable(R.drawable.baseline_pause_24)
        timerStarted = true
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.startStopButton.text = "Start"
        binding.startStopButton.icon = getDrawable(R.drawable.baseline_play_arrow_24)
        timerStarted = true
    }


}