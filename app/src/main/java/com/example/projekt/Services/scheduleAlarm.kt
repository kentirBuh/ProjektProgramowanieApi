package com.example.projekt.Services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.projekt.Model.Recipe
import java.util.Calendar

@SuppressLint("ScheduleExactAlarm")
fun scheduleAlarm(context: Context, recipe: Recipe, triggerTime: Long) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("recipe", recipe)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        recipe.id, // Use a unique request code for each alarm
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        triggerTime,
        pendingIntent
    )
}