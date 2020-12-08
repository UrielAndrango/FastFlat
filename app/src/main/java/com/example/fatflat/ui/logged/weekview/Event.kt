package com.example.fatflat.ui.logged.weekview

import java.util.*

data class Event(
        val id: Long,
        val title: String,
        val startTime: Calendar,
        val endTime: Calendar
)
