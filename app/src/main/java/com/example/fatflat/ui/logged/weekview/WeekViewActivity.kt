package com.example.fatflat.ui.logged.weekview

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.alamkanak.weekview.WeekView
import com.example.fatflat.R
import com.example.fatflat.ui.logged.Profile
import java.util.*

class WeekViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_horario)
        supportActionBar?.hide()
        findViewById<ImageView>(R.id.DeleteAccount_Atras).setOnClickListener {
            onNewIntent(Intent(this, Profile::class.java))
            finish();
        }

        val adapter = WeekViewAdapter()
        val weekView = findViewById<WeekView>(R.id.weekView)
        weekView.adapter = adapter
        val events = mutableListOf<Event>()
        //Monday
        var startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_WEEK] = startTime.firstDayOfWeek
        startTime[Calendar.HOUR_OF_DAY] = 7
        startTime[Calendar.MINUTE] = 0
        var endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 5)
        events.add(Event(1, "Visita", startTime, endTime))
        //Tuesday
        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_WEEK] = startTime.firstDayOfWeek + 1
        startTime[Calendar.HOUR_OF_DAY] = 7
        startTime[Calendar.MINUTE] = 0
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 5)
        events.add(Event(1, "Visita", startTime, endTime))
        // Wednesday
        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_WEEK] = startTime.firstDayOfWeek + 2
        startTime[Calendar.HOUR_OF_DAY] = 7
        startTime[Calendar.MINUTE] = 0
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        events.add(Event(1, "Visita", startTime, endTime))
        // Thursday
        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_WEEK] = startTime.firstDayOfWeek + 3
        startTime[Calendar.HOUR_OF_DAY] = 7
        startTime[Calendar.MINUTE] = 0
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 5)
        events.add(Event(1, "Visita", startTime, endTime))
        // Friday
        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_WEEK] = startTime.firstDayOfWeek + 4
        startTime[Calendar.HOUR_OF_DAY] = 8
        startTime[Calendar.MINUTE] = 0
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 4)
        events.add(Event(1, "Visita", startTime, endTime))
        // Saturday
        startTime = Calendar.getInstance()
        startTime[Calendar.DAY_OF_WEEK] = startTime.firstDayOfWeek + 5
        startTime[Calendar.HOUR_OF_DAY] = 10
        startTime[Calendar.MINUTE] = 0
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 10)
        events.add(Event(1, "Visita", startTime, endTime))
        adapter.submitList(events)
    }
}
