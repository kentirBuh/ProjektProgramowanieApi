package com.example.projekt.Services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.projekt.Model.Recipe
import android.content.pm.PackageManager

class ScheduleAlarm {
    companion object {
        private const val ALARM_REQUEST_CODE_PREFIX = 1000
    }

    @SuppressLint("ObsoleteSdkInt", "NewApi", "ScheduleExactAlarm")
    fun scheduleAlarm(context: Context, recipe: Recipe, triggerTime: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("recipe", recipe)
        }
        val requestCode = recipe.id // Use unique request code
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                // Check if the app has the SCHEDULE_EXACT_ALARM permission
                val packageManager = context.packageManager
                val hasExactAlarmPermission = packageManager.checkPermission(
                    "android.permission.SCHEDULE_EXACT_ALARM",
                    context.packageName
                ) == PackageManager.PERMISSION_GRANTED

                if (hasExactAlarmPermission) {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    // Handle lack of permission gracefully
                    Log.e("ScheduleAlarm", "Cannot schedule exact alarms: permission not granted")
                    // Optionally, notify the user or use approximate alarm scheduling
                }
            } catch (e: Exception) {
                Log.e("ScheduleAlarm", "Error checking exact alarm permission: ${e.message}")
                // Handle exception
            }
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(context: Context, recipe: Recipe) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ALARM_REQUEST_CODE_PREFIX + recipe.id

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE // Pass FLAG_NO_CREATE to get the existing PendingIntent or null if not found
        )

        pendingIntent?.let {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(it)
            it.cancel()
        }
    }
}