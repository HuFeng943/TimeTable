package com.hufeng943.timetable.shared.ui.mappers

import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus

private fun TimeTable.getWeekIndexForDate(date: LocalDate): Int {
    val offsetDays = (date.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
    val currentMonday = date.minus(offsetDays.toLong(), DateTimeUnit.DAY)

    val daysBetween = currentMonday.toEpochDays() - this.semesterStartMonday.toEpochDays()
    if (daysBetween < 0) return 0
    return (daysBetween / 7 + 1).toInt()
}

fun TimeTable.toCourseWithSlots(): List<CourseWithSlotId> =
    allCourses.flatMap { it.toCourseWithSlots() }

fun TimeTable.toDayCourseWithSlots(date: LocalDate): List<CourseWithSlotId> {
    val weekIndex = getWeekIndexForDate(date)
    if (weekIndex == 0) return emptyList()

    val isOddWeek = weekIndex % 2 != 0

    return allCourses.flatMap { course -> course.toDayCourseWithSlots(date, isOddWeek) }
}
