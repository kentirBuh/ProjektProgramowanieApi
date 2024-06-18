package com.example.projekt.ViewModels

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt.Model.Recipe
import com.example.projekt.R
import com.example.projekt.Services.ScheduleAlarm

class DisplayAlarmsActivity : AppCompatActivity() {

    private lateinit var alarmListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var scheduledAlarms: List<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_alarms)

        // Initialize ListView
        alarmListView = findViewById(R.id.alarmListView)

        // Fetch scheduled alarms
        scheduledAlarms = ScheduleAlarm.getScheduledAlarms(this)
        Log.d("DisplayAlarmsActivity", "Alarm names: ${scheduledAlarms.toString()}")
        val alarmNames = scheduledAlarms.map { it.name }

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            alarmNames
        )

        alarmListView.adapter = adapter

        alarmListView.setOnItemClickListener { parent, view, position, id ->
            val selectedRecipe = scheduledAlarms[position]
            showDeleteDialog(selectedRecipe)
        }
    }

    private fun showDeleteDialog(recipe: Recipe) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Alarm")
            .setMessage("Are you sure you want to delete this alarm?")
            .setPositiveButton("Delete") { dialog, which ->
                // User confirmed deletion, call removeAlarm function
                ScheduleAlarm.removeAlarm(this, recipe)
                // Update list view
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