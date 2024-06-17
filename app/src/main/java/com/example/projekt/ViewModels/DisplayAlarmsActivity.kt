package com.example.projekt.ViewModels




import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt.Model.Recipe
import com.example.projekt.R
import com.example.projekt.Services.ScheduleAlarm

class DisplayAlarmsActivity : AppCompatActivity() {

    private lateinit var alarmListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_alarms)

        // Initialize ListView
        alarmListView = findViewById(R.id.alarmListView)

        // Fetch scheduled alarms
        val scheduledAlarms = ScheduleAlarm.getScheduledAlarms(this)
        Log.d("DisplayAlarmsActivity", "Alarm names: ${scheduledAlarms.toString()}")
        val alarmNames = scheduledAlarms.map { it.name }


        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            alarmNames
        )

        alarmListView.adapter = adapter
    }
}