package com.hufeng943.timetable.shared.model

import com.hufeng943.timetable.shared.serializer.InstantAsLongSerializer
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class TimeTable(
    val allCourses: List<Course> = emptyList(),
    val timeTableId: Long = 0,
    val semesterName: String, // 课程表的名称
    @Serializable(with = InstantAsLongSerializer::class)// 这是kotlin.time.Instant
    val createdAt: Instant,
    val semesterStart: LocalDate, // 课表开始日期
    val semesterEnd: LocalDate? = null // 课表结束日期,有可能永不结束
) {
    init {
        require(semesterName.isNotBlank()) { "Semester name cannot be blank." }
        require(
            semesterStart < (semesterEnd ?: LocalDate(
                9999, 12, 31
            ))
        ) { "Invalid semester dates." }
    }

    // 取课表开始当周的周一
    val semesterStartMonday: LocalDate by lazy {
        val offsetDays =
            (semesterStart.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
        semesterStart.minus(offsetDays.toLong(), DateTimeUnit.DAY)
    }

    // 快速查找 Course 对象的 Map
    val courseMap: Map<Long, Course> by lazy {
        allCourses.associateBy { it.id }
    }

    // 快速查找 TimeSlot 对象的 Map，扁平化所有课程的时间段
    val allTimeSlotsMap: Map<Long, TimeSlot> by lazy {
        allCourses.flatMap { course ->
            course.timeSlots.map { slot ->
                slot.id to slot
            }
        }.toMap()
    }
}
