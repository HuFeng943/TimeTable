package com.hufeng943.timetable.presentation.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.pagers.CourseDetailPager
import com.hufeng943.timetable.presentation.ui.screens.HomeScreen
import com.hufeng943.timetable.presentation.ui.screens.LoadingScreen
import com.hufeng943.timetable.presentation.utils.loadData
import com.hufeng943.timetable.shared.data.dao.TimeTableDao
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId

@Composable
fun AppNavHost(dao: TimeTableDao) {
    val navController = rememberSwipeDismissableNavController()

    val timeTableState: MutableState<TimeTable?> = remember { mutableStateOf(null) }
    val timeTable: TimeTable? by timeTableState

    val todayCoursesIdListState: MutableState<List<CourseWithSlotId>?> =
        remember { mutableStateOf(null) }
    val todayCoursesIdList: List<CourseWithSlotId>? by todayCoursesIdListState

    // 异步加载课程：调用外部的实用函数
    LaunchedEffect(Unit) {
        loadData(dao, timeTableState)
        // 导航到主屏幕
        navController.navigate("main") {
            Log.d("loadData","加载完成~")
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
            if (timeTable != null && todayCoursesIdList != null) {
                HomeScreen(navController, timeTable!!, todayCoursesIdList!!)
            } else {
                // TODO 单独一个异常界面
                LoadingScreen()
            }
        }
        composable("course_detail/{courseId}/{timeSlotId}") { backStackEntry ->
            val courseId = backStackEntry.longArg("courseId")
            val timeSlotId = backStackEntry.longArg("timeSlotId")

            if (timeTable != null && courseId != null && timeSlotId != null) {
                CourseDetailPager(
                    timeTable,
                    CourseWithSlotId(courseId, timeSlotId)
                )
            } else {
                // TODO 单独一个异常界面
                LoadingScreen()
            }

        }
    }
}

// 对一堆getString()的封装
fun NavBackStackEntry.longArg(key: String): Long? =
    arguments?.getString(key)?.toLongOrNull()