package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt.Model.Recipe
import com.example.projekt.Services.ScheduleAlarm
import com.example.projekt.Services.createNotificationChannel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import java.util.*

class RecipeTimer : AppCompatActivity() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var submitButton: Button

    private lateinit var selectedRecipe: Recipe
    private lateinit var scheduleAlarm: ScheduleAlarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_timer)

        Log.d("NotificationTest", "Before notif chanel is created")
        createNotificationChannel(this)
        Log.d("NotificationTest", "After notif chanel is created")

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        datePicker = findViewById(R.id.datePicker)
        timePicker = findViewById(R.id.timePicker)
        submitButton = findViewById(R.id.submitButton)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_timer -> {
                    true
                }

                R.id.navigation_create -> {
                    startActivity(Intent(this@RecipeTimer, MainActivity::class.java))
                    true
                }

                R.id.navigation_view -> {
                    startActivity(Intent(this@RecipeTimer, RecipeListActivity::class.java))
                    true
                }

                else -> false
            }
        })

        scheduleAlarm = ScheduleAlarm()

        // Simulated list of recipes; replace with your actual list or network call
        val recipes = listOf(
            Recipe(1, "Recipe 1", 30, "Instructions", emptyList()),
            Recipe(2, "Recipe 2", 45, "Instructions", emptyList())
            // Add more recipes as needed
        )

        val recipeNames = recipes.map { it.name }.toTypedArray()
        val autoCompleteAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            recipeNames
        )
        autoCompleteTextView.setAdapter(autoCompleteAdapter)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedRecipe = recipes[position]
        }

        submitButton.setOnClickListener {
            Log.d("NotificationTest", "button is clicked")
            val calendar = Calendar.getInstance().apply {
                set(datePicker.year, datePicker.month, datePicker.dayOfMonth,
                    timePicker.hour, timePicker.minute, 0)
            }
            Log.d("timeinMilis", calendar.timeInMillis.toString())
            scheduleAlarm.scheduleAlarm(this, selectedRecipe, calendar.timeInMillis)
        }
    }
}