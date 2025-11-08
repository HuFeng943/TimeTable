package com.hufeng943.timetable.presentation.ui

import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.theme.TimeTableTheme
import com.hufeng943.timetable.shared.Course
import com.hufeng943.timetable.shared.CourseUi
import com.hufeng943.timetable.shared.TimeSlot
import com.hufeng943.timetable.shared.TimeTable
import com.hufeng943.timetable.shared.WeekPattern
import com.hufeng943.timetable.shared.toDayUiCourses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun MainNavHost() {
    val navController = rememberSwipeDismissableNavController() // Horologist
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "nowTimetable"
    ) {
        composable("nowTimetable") {
            MainContent(navController)
        }
        composable("courseDetail/{courseId}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")?.toInt()
            CourseDetailScreen(courseId = courseId)
        }
    }
}
@Composable // 测试用
fun CourseDetailScreen(courseId: Int?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "课程详情页: $courseId")
    }
}


@Composable
fun MainContent(navController: NavHostController) {
    TimeTableTheme {
        // 异步加载课程数据
        val coursesState: MutableState<List<CourseUi>?> = remember {
            mutableStateOf(null)
        }
        val courses: List<CourseUi>? by coursesState
        LaunchedEffect(Unit) {
            val sample = withContext(Dispatchers.Default) {
                // TODO 读取本地课程表，返回 List<CourseUi>
                getSampleTimetable().toDayUiCourses(LocalDate(2025, 11, 1))
            }
            coursesState.value = sample
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = rememberPagerState { 10 }
        ) { page ->
            when (page) {
                0 -> TimetableScreen(
                    courses = courses,
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
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = "占位符这 $page 块",
            style = TextStyle(color = Color.White)
        )
    }
}


// 临时示例数据
private fun getSampleTimetable(): TimeTable {
    return TimeTable(
        allCourses = listOf(
            Course(
                id = 1145,
                name = "物理5 (双周)",
                timeSlots = listOf(
                    TimeSlot(
                        startTime = LocalTime(13, 30),
                        endTime = LocalTime(15, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    )
                ),
                recurrence = WeekPattern.EVEN_WEEK, // 双周
                teacher = "李老师",
                location = "B202",
                color = 0xFF6750A4
            ),
            Course(
                id = 11919,
                name = "低等数学",
                timeSlots = listOf(
                    TimeSlot(
                        startTime = LocalTime(9, 0),
                        endTime = LocalTime(11, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    ), TimeSlot(
                        startTime = LocalTime(19, 0),
                        endTime = LocalTime(22, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    ), TimeSlot(
                        startTime = LocalTime(13, 30),
                        endTime = LocalTime(15, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    )
                ),
                recurrence = WeekPattern.ODD_WEEK, // 增加：单周课
                teacher = "王老师",
                location = "A101",
                color = 0xFF6750A4
            ),
            Course(
                id = 810,
                name = "English",
                timeSlots = listOf(
                    // 周六的英语课
                    TimeSlot(
                        startTime = LocalTime(15, 30),
                        endTime = LocalTime(17, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    ), TimeSlot(
                        startTime = LocalTime(13, 30),
                        endTime = LocalTime(14, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    )
                ),
                recurrence = WeekPattern.EVERY_WEEK, // 每周都上
                teacher = "赵老师",
                location = "C303",
                color = 0xFF625B71
            )
        ),
        semesterName = "2025秋季",
        createdAt = kotlin.time.Clock.System.now(),
        semesterStart = LocalDate(2025, 10, 1), // 2025-10-01 是周三
        timeTableId = 114514,
        semesterEnd = null
    )
}