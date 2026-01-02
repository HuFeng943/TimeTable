package com.hufeng943.timetable.presentation.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
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
    CompositionLocalProvider(LocalNavController provides navController) {
        AppScaffold {
            SwipeDismissableNavHost(
                navController = navController, startDestination = NavRoutes.LOADING
            ) {

                composable(NavRoutes.LOADING) {
                    LoadingScreen(timeTables)
                }
                composable(NavRoutes.ERROR) {
                    Log.v("navController", NavRoutes.ERROR)
                    TODO()// TODO 单独一个异常界面 可以传递的不同的相关的错误信息
                }
                composable(NavRoutes.MAIN) {
                    Log.v("navController1", (timeTables == null).toString())
                    timeTables?.let { tables ->
                        HomeScreen(tables)
                    } ?: LoadingScreen(timeTables)
                }
                composable(NavRoutes.COURSE_DETAIL) { backStackEntry ->
                    timeTables?.let { tables ->
                        val courseId = backStackEntry.longArg("courseId")
                        val timeSlotId = backStackEntry.longArg("timeSlotId")
                        if (courseId != null && timeSlotId != null) {
                            val timeTable: TimeTable? = tables.firstOrNull()
                            CourseDetailScreen(
                                timeTable, CourseWithSlotId(courseId, timeSlotId)
                            )
                        } else {
                            navController.navigate(NavRoutes.ERROR)
                            // 这里才是真的出错了
                        }
                    } ?: LoadingScreen(timeTables)
                }
                composable(NavRoutes.EDIT_COURSE) {
                    EditCourseScreen()
                }
            }
        }
    }
}

// 对一堆getString()的封装
fun NavBackStackEntry.longArg(key: String): Long? = arguments?.getString(key)?.toLongOrNull()