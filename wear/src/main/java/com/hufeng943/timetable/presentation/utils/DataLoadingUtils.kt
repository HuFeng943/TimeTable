package com.hufeng943.timetable.presentation.utils

import androidx.compose.runtime.MutableState
import android.util.Log
import com.hufeng943.timetable.shared.data.dao.TimeTableDao
import com.hufeng943.timetable.shared.data.mappers.toPersistenceBundle
import com.hufeng943.timetable.shared.data.mappers.toTimeTable
import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Instant

suspend fun loadData(
    dao: TimeTableDao,
    timeTableState: MutableState<TimeTable?>
) {
    // val today = LocalDate(2025, 11, 1)

    val loadedTimetable = withContext(Dispatchers.IO) {

        // 1. 从 DAO 读取所有课表的关系对象 (Flow 仅用于演示读取)
        // 我们使用 Flow.firstOrNull() 来立即获取当前值
        val relations = dao.getAllTimeTablesWithCourses().firstOrNull()

        // 2. 检查数据库是否有数据
        val existingTableRelation = relations?.firstOrNull()

        if (existingTableRelation == null) {
            Log.i("loadData","数据库为空，用个模板顶上")
            val sampleTable = getSampleTimetable()
            // 2.1. 调用 Mappers 转换为 Entity Bundle
            val bundle = sampleTable.toPersistenceBundle()

            // 2.2. 事务操作：直接使用 DAO 插入数据
            dao.insertTimeTable(bundle.timeTable)
            dao.insertCourses(bundle.courses)
            dao.insertTimeSlots(bundle.timeSlots)

            return@withContext sampleTable // 返回我们刚刚插入的示例数据
        } else {
            Log.i("loadData","数据库已有数据")
            return@withContext existingTableRelation.toTimeTable()
        }
    }
    timeTableState.value = loadedTimetable

    //val weekIndex = loadedTimetable.getWeekIndexForDate(today)
    //todayCoursesIdListState.value = loadedTimetable.toDayCourseWithSlots(today.dayOfWeek, weekIndex)

}

private fun getSampleTimetable(): TimeTable = TimeTable(
    allCourses = listOf(
        Course(
            id = 1145, name = "物理5 (双周)", timeSlots = listOf(
                TimeSlot(
                    id = 0,
                    startTime = LocalTime(13, 30),
                    endTime = LocalTime(15, 0),
                    dayOfWeek = DayOfWeek.SATURDAY,
                    recurrence = WeekPattern.EVEN_WEEK // 双周
                )
            ), teacher = "李老师", location = "B202", color = 0xFF6750A4
        ), Course(
            id = 11919, name = "低等数学", timeSlots = listOf(
                TimeSlot(
                    id = 1,
                    startTime = LocalTime(9, 0),
                    endTime = LocalTime(11, 0),
                    dayOfWeek = DayOfWeek.SATURDAY,
                    recurrence = WeekPattern.ODD_WEEK // 增加：单周课
                ), TimeSlot(
                    id = 2,
                    startTime = LocalTime(19, 0),
                    endTime = LocalTime(22, 0),
                    dayOfWeek = DayOfWeek.SATURDAY,
                    recurrence = WeekPattern.ODD_WEEK
                ), TimeSlot(
                    id = 3,
                    startTime = LocalTime(10, 30),
                    endTime = LocalTime(15, 0),
                    dayOfWeek = DayOfWeek.SATURDAY,
                    recurrence = WeekPattern.ODD_WEEK
                )
            ), teacher = "王老师", location = "A101", color = 0xFF6750A4
        ), Course(
            id = 810, name = "English", timeSlots = listOf(
                // 周六的英语课
                TimeSlot(
                    id = 4,
                    startTime = LocalTime(15, 30),
                    endTime = LocalTime(17, 0),
                    dayOfWeek = DayOfWeek.SATURDAY,
                    recurrence = WeekPattern.EVERY_WEEK // 每周都上
                ), TimeSlot(
                    id = 5,
                    startTime = LocalTime(13, 30),
                    endTime = LocalTime(14, 0),
                    dayOfWeek = DayOfWeek.SATURDAY,
                    recurrence = WeekPattern.EVERY_WEEK
                )
            ), teacher = "赵老师", location = "C303", color = 0xFF625B71
        )
    ),
    semesterName = "2025秋季",
    createdAt = Instant.parse("2025-01-01T00:00:00Z"),
    semesterStart = LocalDate(2025, 10, 1), // 2025-10-01 是周三
    timeTableId = 114514,
    semesterEnd = null
)