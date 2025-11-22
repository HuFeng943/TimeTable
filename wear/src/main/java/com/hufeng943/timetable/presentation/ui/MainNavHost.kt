package com.hufeng943.timetable.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.theme.TimeTableTheme
import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.model.WeekPattern
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import com.hufeng943.timetable.shared.ui.mappers.getWeekIndexForDate
import com.hufeng943.timetable.shared.ui.mappers.toCourseUi
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
        todayCoursesIdListState.value = sampleTimetable.toDayCourseWithSlots(today.dayOfWeek,weekIndex)

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
            val courseId = backStackEntry.arguments!!.getString("courseId")?.toLong()
            val timeSlotId = backStackEntry.arguments!!.getString("timeSlotId")?.toLong()
            CourseDetailScreen(timeTable, courseId, timeSlotId)
        }
    }
}

@Composable // TODO 这个也是单独提取出来
fun CourseDetailScreen(timeTable: TimeTable?, courseId: Long?, timeSlotId: Long?) {
    val courseUi =
        if (timeTable != null && courseId != null && timeSlotId != null) timeTable.toCourseUi(
            CourseWithSlotId(courseId, timeSlotId)
        )
        else null

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (courseUi != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = courseUi.name, fontWeight = FontWeight.Bold)
                Text(text = "教师：${courseUi.teacher ?: "无"}")
                Text(text = "地点：${courseUi.location ?: "未填写"}")
                Text(
                    text = "时间：${
                        courseUi.timeSlot.dayOfWeek.name.substring(
                            0, 3
                        )
                    } ${courseUi.timeSlot.startTime} - ${courseUi.timeSlot.endTime}"
                )
                Text(text = "重复：${courseUi.timeSlot.recurrence}")
            }
        } else {
            Text(text = "未找到课程数据", color = Color.Red)
        }
    }
}

@Composable // TODO 单独出来
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("课程加载中…")
    }
}


@Composable
fun HomeScreen(
    navController: NavHostController,
    timeTable: TimeTable,
    todayCoursesIdList: List<CourseWithSlotId>
) {
    TimeTableTheme {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(), state = rememberPagerState { 10 }) { page ->
            when (page) {
                0 -> TimetableScreen(
                    timeTable = timeTable, // 传递 TimeTable
                    coursesIdList = todayCoursesIdList,
                    title = "哈基米",
                    navController = navController
                )
                else -> PagePlaceholder(page)
            }
        }
    }
}


@Composable
private fun PagePlaceholder(page: Int) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = "占位符这 $page 块", style = TextStyle(color = Color.White)
        )
    }
}


// 临时示例数据
private fun getSampleTimetable(): TimeTable {
    return TimeTable(
        allCourses = listOf(
            Course(
                id = 1145, name = "物理5 (双周)", timeSlots = listOf(
                    TimeSlot(
                        startTime = LocalTime(13, 30),
                        endTime = LocalTime(15, 0),
                        dayOfWeek = DayOfWeek.SATURDAY,
                        recurrence = WeekPattern.EVEN_WEEK // 双周
                    )
                ), teacher = "李老师", location = "B202", color = 0xFF6750A4
            ), Course(
                id = 11919, name = "低等数学", timeSlots = listOf(
                    TimeSlot(
                        startTime = LocalTime(9, 0),
                        endTime = LocalTime(11, 0),
                        dayOfWeek = DayOfWeek.SATURDAY,
                        recurrence = WeekPattern.ODD_WEEK // 增加：单周课
                    ), TimeSlot(
                        startTime = LocalTime(19, 0),
                        endTime = LocalTime(22, 0),
                        dayOfWeek = DayOfWeek.SATURDAY,
                        recurrence = WeekPattern.ODD_WEEK
                    ), TimeSlot(
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
                        startTime = LocalTime(15, 30),
                        endTime = LocalTime(17, 0),
                        dayOfWeek = DayOfWeek.SATURDAY,
                        recurrence = WeekPattern.EVERY_WEEK // 每周都上
                    ), TimeSlot(
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
