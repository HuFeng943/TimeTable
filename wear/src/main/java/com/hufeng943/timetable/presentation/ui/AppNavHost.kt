package com.hufeng943.timetable.presentation.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.NavRoutes
import com.hufeng943.timetable.presentation.ui.screens.coursedetail.CourseDetailScreen
import com.hufeng943.timetable.presentation.ui.screens.editcourse.EditCourseScreen
import com.hufeng943.timetable.presentation.ui.screens.home.HomeScreen
import com.hufeng943.timetable.presentation.ui.screens.loading.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.TimeTableViewModel
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId

@Composable
fun AppNavHost(viewModel: TimeTableViewModel) {
    val navController = rememberSwipeDismissableNavController()

    // 订阅 Flow -> Compose state
    val timeTables by viewModel.timeTables.collectAsState()


    SwipeDismissableNavHost(
        navController = navController, startDestination = NavRoutes.LOADING
    ) {
        composable(NavRoutes.LOADING) {
            LoadingScreen()
            if (timeTables != null) {
                navController.navigate(NavRoutes.MAIN) {
                    popUpTo(NavRoutes.LOADING) { inclusive = true }
                    Log.v("navController", "加载完成，跳转！")
                }
            }

        }
        composable(NavRoutes.MAIN) {
            if (timeTables != null) {
                HomeScreen(navController, timeTables!!)
            } else {
                // TODO 单独一个异常界面
                LoadingScreen()
            }
        }
        composable(NavRoutes.COURSE_DETAIL) { backStackEntry ->
            val currentTables = timeTables
            val courseId = backStackEntry.longArg("courseId")
            val timeSlotId = backStackEntry.longArg("timeSlotId")
            if (currentTables != null && courseId != null && timeSlotId != null) {
                val timeTable: TimeTable? = currentTables.firstOrNull()
                CourseDetailScreen(
                    timeTable, CourseWithSlotId(courseId, timeSlotId)
                )
            } else {
                // TODO 单独一个异常界面
                LoadingScreen()
            }
        }
        composable(NavRoutes.EDIT_COURSE){
            EditCourseScreen()
        }
    }
}

// 对一堆getString()的封装
fun NavBackStackEntry.longArg(key: String): Long? = arguments?.getString(key)?.toLongOrNull()