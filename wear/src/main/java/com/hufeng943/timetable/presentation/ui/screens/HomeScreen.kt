package com.hufeng943.timetable.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import com.hufeng943.timetable.presentation.ui.pagers.TimetablePager
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId

@Composable
fun HomeScreen(
    navController: NavHostController,
    timeTable: TimeTable,
    todayCoursesIdList: List<CourseWithSlotId>
) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize(), state = rememberPagerState { 10 }) { page ->
        when (page) {
            0 -> TimetablePager(
                timeTable = timeTable, // 传递 TimeTable
                coursesIdList = todayCoursesIdList, title = "今日程", navController = navController
            )
            else -> PagePlaceholder(page)
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