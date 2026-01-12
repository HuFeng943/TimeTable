package com.hufeng943.timetable.presentation.ui.screens.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import com.hufeng943.timetable.shared.ui.mappers.toCourseUi

@Composable
fun CourseDetailScreen(timeTable: TimeTable?, courseWithSlotId: CourseWithSlotId?) {
    AppScaffold {
        val courseId = if (timeTable != null && courseWithSlotId != null) {
            timeTable.toCourseUi(
                CourseWithSlotId(
                    courseWithSlotId.courseId, courseWithSlotId.timeSlotId
                )
            )
        } else null
        if (courseId != null) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(), state = rememberPagerState { 2 }) { page ->
                when (page) {
                    0 -> Details(courseId)
                    1 -> CourseList(courseId)
                }
            }
        } else Text(text = "未找到课程数据", color = Color.Red)
    }
}