package com.example.projekt.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

fun createNotificationChannel(context: Context) { //Needed to create notifications
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Recipe Channel"
        val descriptionText = "Channel for recipe notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("recipe_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}