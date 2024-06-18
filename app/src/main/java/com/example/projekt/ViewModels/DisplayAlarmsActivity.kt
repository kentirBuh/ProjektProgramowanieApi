package com.example.projekt.ViewModels

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt.MainActivity
import com.example.projekt.Model.Recipe
import com.example.projekt.R
import com.example.projekt.RecipeListActivity
import com.example.projekt.RecipeTimer
import com.example.projekt.Services.ScheduleAlarm
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView

class DisplayAlarmsActivity : AppCompatActivity() {//responsible for displaying all current alarms, updating the list
    // , and deleting them from the UI level

    private lateinit var alarmListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var scheduledAlarms: List<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_alarms)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)



        alarmListView = findViewById(R.id.alarmListView)

        //Get alarms
        scheduledAlarms = ScheduleAlarm.getScheduledAlarms(this)
        Log.d("DisplayAlarmsActivity", "Alarm names: ${scheduledAlarms.toString()}")
        val alarmNames = scheduledAlarms.map { it.name }

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            alarmNames
        )

        alarmListView.adapter = adapter

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, RecipeTimer::class.java))
        }

        alarmListView.setOnItemClickListener { parent, view, position, id ->
            val selectedRecipe = scheduledAlarms[position]
            showDeleteDialog(selectedRecipe)
        }

        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_timer -> {
                    startActivity(Intent(this@DisplayAlarmsActivity, RecipeTimer::class.java))
                    true
                }

                R.id.navigation_create -> {
                    startActivity(Intent(this@DisplayAlarmsActivity, MainActivity::class.java))
                    true
                }

                R.id.navigation_view -> {
                    startActivity(Intent(this@DisplayAlarmsActivity, RecipeListActivity::class.java))
                    true
                }

                else -> false
            }
        })
    }

    private fun showDeleteDialog(recipe: Recipe) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Alarm")
            .setMessage("Are you sure you want to delete this alarm?")
            .setPositiveButton("Delete") { dialog, which ->
                ScheduleAlarm.removeAlarm(this, recipe)
                scheduledAlarms = ScheduleAlarm.getScheduledAlarms(this)
                updateListView()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateListView() {
        val alarmNames = scheduledAlarms.map { it.name }
        adapter.clear()
        adapter.addAll(alarmNames)
        adapter.notifyDataSetChanged()
    }
}