package com.hufeng943.timetable.shared.ui.mappers

import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.WeekPattern
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import kotlinx.datetime.LocalDate

fun Course.toCourseWithSlots(): List<CourseWithSlotId> = timeSlots.map { slot ->
    CourseWithSlotId(
        courseID = this.id, timeSlotID = slot.id
    )
}

fun Course.toDayCourseWithSlots(date: LocalDate, isOddWeek: Boolean): List<CourseWithSlotId> =
    timeSlots.filter { it.dayOfWeek == date.dayOfWeek }.filter { slot ->
            when (slot.recurrence) {
                WeekPattern.EVERY_WEEK -> true
                WeekPattern.ODD_WEEK -> isOddWeek
                WeekPattern.EVEN_WEEK -> !isOddWeek
            }
        }.map { slot ->
            CourseWithSlotId(
                courseID = this.id, timeSlotID = slot.id
            )
        }
