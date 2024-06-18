package com.example.projekt.Services


import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.projekt.Model.Recipe
import com.example.projekt.R
import com.example.projekt.RecipeDetailActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) { //used for handling alarms and deleting them from the schedule
        Log.e("Received?", "Yes")
        val recipe = intent.getParcelableExtra<Recipe>("recipe")

        if (recipe != null) {
            showNotification(context, recipe)

            GlobalScope.launch(Dispatchers.IO) {
                ScheduleAlarm.removeAlarm(context, recipe)
            }
        } else {
            Log.e("AlarmReceiver", "Recipe is null")
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, recipe: Recipe) {
        Log.e("Showing notification", "1")
        val intent = Intent(context, RecipeDetailActivity::class.java).apply {
            putExtra("recipe", recipe)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            recipe.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "recipe_channel")
            .setContentTitle("Time to Cook!")
            .setContentText("Your recipe for ${recipe.name} is ready.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_launcher_foreground, "Open Recipe", pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(recipe.id, notification)
    }
}