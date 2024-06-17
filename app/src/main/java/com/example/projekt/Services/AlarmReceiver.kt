package com.example.projekt.Services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.projekt.Model.Recipe
import com.example.projekt.R
import com.example.projekt.RecipeDetailActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val recipe = intent.getParcelableExtra<Recipe>("recipe")
        if (recipe != null) {
            showNotification(context, recipe)
        }
    }


    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, recipe: Recipe) {
        val intent = Intent(context, RecipeDetailActivity::class.java).apply {
            putExtra("recipe", recipe)
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
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(1, "Open Recipe", pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(recipe.id, notification)
    }
}























