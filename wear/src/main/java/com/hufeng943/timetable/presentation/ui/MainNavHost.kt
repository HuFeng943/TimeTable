package com.hufeng943.timetable.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.pagers.CourseDetailPager
import com.hufeng943.timetable.presentation.ui.screens.HomeScreen
import com.hufeng943.timetable.presentation.ui.screens.LoadingScreen
import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.model.WeekPattern
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import com.hufeng943.timetable.shared.ui.mappers.getWeekIndexForDate
import com.hufeng943.timetable.shared.ui.mappers.toDayCourseWithSlots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Clock

@Composable
fun AppNavHost() {// 数据由AppNavHost读取
    val navController = rememberSwipeDismissableNavController()

    val timeTableState: MutableState<TimeTable?> = remember { mutableStateOf(null) }
    val timeTable: TimeTable? by timeTableState

    val todayCoursesIdListState: MutableState<List<CourseWithSlotId>?> =
        remember { mutableStateOf(null) }
    val todayCoursesIdList: List<CourseWithSlotId>? by todayCoursesIdListState


    // 异步加载课程
    LaunchedEffect(Unit) {
        delay(1000)
        val today = LocalDate(2025, 11, 1)
        val sampleTimetable = withContext(Dispatchers.Default) {
            // TODO 这里应该要换成个实列的
            getSampleTimetable()
        }

        timeTableState.value = sampleTimetable
        val weekIndex = sampleTimetable.getWeekIndexForDate(today)
        todayCoursesIdListState.value =
            sampleTimetable.toDayCourseWithSlots(today.dayOfWeek, weekIndex)

        // ok加载完了好吧
        navController.navigate("main") {
            popUpTo("loading") { inclusive = true }
        }
    }

    SwipeDismissableNavHost(
        navController = navController, startDestination = "loading"
    ) {
        composable("loading") {
            LoadingScreen()
        }
        composable("main") {
            HomeScreen(navController, timeTable!!, todayCoursesIdList!!)
        }
        composable("course_detail/{courseId}/{timeSlotId}") { backStackEntry ->
            val courseWithSlotId = CourseWithSlotId(
                courseId = backStackEntry.arguments!!.getString("courseId")!!.toLong(),
                timeSlotId = backStackEntry.arguments!!.getString("timeSlotId")!!.toLong()
            )
            CourseDetailPager(timeTable, courseWithSlotId)
        }
    }
}

// 临时示例数据
private fun getSampleTimetable(): TimeTable {
    return TimeTable(
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
        createdAt = Clock.System.now(),
        semesterStart = LocalDate(2025, 10, 1), // 2025-10-01 是周三
        timeTableId = 114514,
        semesterEnd = null
    )
}
