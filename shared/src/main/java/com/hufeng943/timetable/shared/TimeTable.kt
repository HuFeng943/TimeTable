package com.hufeng943.timetable.shared

// kotlinx.serialization
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder

// kotlinx.datetime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus

import kotlin.time.Instant // kotlin2.2中datetime.Instant已经废弃

// kotlinx.serialization 还没原生支持 kotlin.time.Instant,先自定义个序列化器
object InstantAsLongSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("InstantAsLong", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeLong(value.toEpochMilliseconds())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.fromEpochMilliseconds(decoder.decodeLong())
    }
}

@Serializable
enum class WeekPattern {//单双周课程
    EVERY_WEEK, ODD_WEEK, EVEN_WEEK
}

@Serializable
data class TimeSlot(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val dayOfWeek: DayOfWeek
) {
    init {
        require(endTime > startTime) { "End time must be after start time." }
    }
}

@Serializable
data class Course(
    val id: Long = 0,
    val name: String,
    val timeSlots: List<TimeSlot>,
    val recurrence: WeekPattern = WeekPattern.EVERY_WEEK, // 默认每周重复
    val location: String?,
    val color: Int = 0xFF2196F3.toInt(), // 默认蓝色
    val teacher: String?
) {
    init {
        require(name.isNotBlank()) { "Course name cannot be blank." }
        require(timeSlots.isNotEmpty()) { "Course must have at least one time slot." }
    }
}

@Serializable
data class UserTimeTable(
    val allCourses: List<Course>,
    val timeTableId: Long = 0,
    val semesterName: String, // 课程表的名称
    @Serializable(with = InstantAsLongSerializer::class)// 这是kotlin.time.Instant
    val createdAt: Instant,
    val semesterStart: LocalDate // 课表开始日期
) {
    init {
        require(semesterName.isNotBlank()) { "Course name cannot be blank." }
    }
    // 取课表开始当周的周一
    private val semesterStartMonday: LocalDate by lazy {
        val offsetDays = (semesterStart.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
        semesterStart.minus(offsetDays.toLong(), DateTimeUnit.DAY)
    }

    private fun getWeekIndex(date: LocalDate): Int {

        val offsetDays = (date.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
        val currentMonday = date.minus(offsetDays.toLong(), DateTimeUnit.DAY)

        val daysBetween = currentMonday.toEpochDays() - semesterStartMonday.toEpochDays()
        if (daysBetween < 0) return 0
        return (daysBetween / 7 + 1).toInt()
    }


    fun getCoursesForDate(date: LocalDate): List<Pair<TimeSlot, Course>> {
        val weekIndex = getWeekIndex(date)
        if (weekIndex == 0) return emptyList() // 课程还没开始
        val isOddWeek = weekIndex % 2 != 0  // 奇数周

        return allCourses
            .filter { course ->
                // 过滤奇偶周
                when (course.recurrence) {
                    WeekPattern.EVERY_WEEK -> true
                    WeekPattern.ODD_WEEK -> isOddWeek
                    WeekPattern.EVEN_WEEK -> !isOddWeek
                }
            }
            .flatMap { course ->
                course.timeSlots
                    .filter { it.dayOfWeek == date.dayOfWeek }
                    .map { it to course } // 将 TimeSlot 和 Course 配对
            }
            .sortedBy { it.first.startTime } // 按照开始时间排序
    }
}