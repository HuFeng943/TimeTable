package com.hufeng943.timetable.shared.model

import kotlinx.serialization.Serializable

@Serializable
enum class WeekPattern { // 单双周课程
    EVERY_WEEK, ODD_WEEK, EVEN_WEEK
}

