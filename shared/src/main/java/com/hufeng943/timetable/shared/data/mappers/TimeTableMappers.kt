package com.hufeng943.timetable.shared.data.mappers

import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.data.entities.CourseEntity
import com.hufeng943.timetable.shared.data.entities.TimeSlotEntity
import com.hufeng943.timetable.shared.data.entities.TimeTableEntity
import com.hufeng943.timetable.shared.data.relations.TimeTableWithCourses
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

data class PersistenceBundle(
    val timeTable: TimeTableEntity,
    val courses: List<CourseEntity>,
    val timeSlots: List<TimeSlotEntity>
)

fun TimeTable.toPersistenceBundle(): PersistenceBundle {
    val timeTableId = this.timeTableId

    // 转换 TimeTableEntity
    val timeTableEntity = TimeTableEntity(
        id = timeTableId,
        semesterName = this.semesterName,
        // Instant -> Long
        createdAtMillis = this.createdAt.toEpochMilliseconds(),
        // LocalDate -> Long
        semesterStartEpochDay = this.semesterStart.toEpochDays(),
        semesterEndEpochDay = this.semesterEnd?.toEpochDays()
    )

    // 转换 CourseEntity 列表
    val courseEntities = this.allCourses.map { course ->
        CourseEntity(
            id = course.id,
            timeTableId = timeTableId,
            name = course.name,
            location = course.location,
            color = course.color,
            teacher = course.teacher
        )
    }

    // 转换 TimeSlotEntity 列表
    val slotEntities = this.allCourses.flatMap { course ->
        course.timeSlots.map { slot ->
            slot.toTimeSlotEntity(course.id) // 调用 TimeSlot.toEntity
        }
    }

    return PersistenceBundle(timeTableEntity, courseEntities, slotEntities)
}

// 核心函数：将嵌套的 Relation 对象组装回 TimeTable
fun TimeTableWithCourses.toTimeTable(): TimeTable {
    val tableEntity = this.timeTable

    // Long (毫秒) -> Instant
    val createdAt = Instant.fromEpochMilliseconds(tableEntity.createdAtMillis)

    // Long (Epoch Day) -> LocalDate
    val semesterStart = LocalDate.fromEpochDays(tableEntity.semesterStartEpochDay.toInt())
    val semesterEnd = tableEntity.semesterEndEpochDay?.toInt()?.let { LocalDate.fromEpochDays(it) }

    return TimeTable(
        timeTableId = tableEntity.id,
        semesterName = tableEntity.semesterName,
        createdAt = createdAt,
        semesterStart = semesterStart,
        semesterEnd = semesterEnd,
        allCourses = this.courses.map { it.toCourse() }
    )
}