package com.example.fatflat.ui.logged.weekview

import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEntity

class WeekViewAdapter : WeekView.SimpleAdapter<Event>() {

    override fun onCreateEntity(item: Event): WeekViewEntity {
        return WeekViewEntity.Event.Builder(item)
                .setId(item.id)
                .setTitle(item.title)
                .setStartTime(item.startTime)
                .setEndTime(item.endTime)
                .build()
    }
}