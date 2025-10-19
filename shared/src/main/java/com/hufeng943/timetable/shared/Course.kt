package com.hufeng943.timetable.shared

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalTime

@Serializable
data class Course(
    val id: Long = 0,
    val name: String,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val dayOfWeek: Byte,     // 1=周一 到 7=周日
    val location: String?,
    val color: Int          // ARGB 颜色
)