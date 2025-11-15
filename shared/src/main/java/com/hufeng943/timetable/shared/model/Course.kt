package com.hufeng943.timetable.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class Course( // 科目
    val id: Long = 0,
    val name: String,
    val timeSlots: List<TimeSlot> = emptyList(),
    val location: String? = null,
    val color: Long = 0xFF2196F3, // 默认蓝色
    val teacher: String? = null
) {
    init {
        require(name.isNotBlank()) { "Course name cannot be blank." }
    }
}
