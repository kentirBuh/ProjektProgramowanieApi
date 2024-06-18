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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScheduleAlarm { //used for linking Recipe objects to alarms
    companion object {
        private const val ALARM_REQUEST_CODE_PREFIX = 1000
        private const val ALARM_PREFS = "alarm_prefs"
        private const val ALARMS_KEY = "alarms"

        fun getScheduledAlarms(context: Context): List<Recipe> {
            val sharedPreferences = context.getSharedPreferences(ALARM_PREFS, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(ALARMS_KEY, null)
            Log.d("ScheduleAlarm", "JSON from SharedPreferences: $json")
            val type = object : TypeToken<List<Recipe>>() {}.type
            return if (json != null) {
                gson.fromJson(json, type)
            } else {
                emptyList()
            }
        }

        @SuppressLint("ObsoleteSdkInt", "NewApi", "ScheduleExactAlarm")
        fun scheduleAlarm(context: Context, recipe: Recipe, triggerTime: Long) {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("recipe", recipe)
            }
            val requestCode = recipe.id
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                try {

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
                        Log.e(
                            "ScheduleAlarm",
                            "Cannot schedule exact alarms: permission not granted"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("ScheduleAlarm", "Error checking exact alarm permission: ${e.message}")
                }
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            val sharedPreferences = context.getSharedPreferences(ALARM_PREFS, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val alarms = getScheduledAlarms(context).toMutableList().apply {
                add(recipe)
            }
            val gson = Gson()
            editor.putString(ALARMS_KEY, gson.toJson(alarms))
            editor.apply()
        }

        fun removeAlarm(context: Context, recipe: Recipe) {
            val sharedPreferences = context.getSharedPreferences(ALARM_PREFS, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val alarms = getScheduledAlarms(context).toMutableList()
            alarms.removeAll { alarm -> alarm.id == recipe.id }
            val gson = Gson()
            editor.putString(ALARMS_KEY, gson.toJson(alarms))
            editor.apply()
        }

    }
}