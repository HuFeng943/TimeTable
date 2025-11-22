package com.hufeng943.timetable.presentation.ui

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.CardDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseUi
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import com.hufeng943.timetable.shared.ui.mappers.toCourseUi
import kotlinx.datetime.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeFormatters {
    val formatter24h: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    }
    val formatter12h: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern("hh:mm", Locale.getDefault())
    }
    val amPmFormatter: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern("a", Locale.US)
    }
}

@Composable
fun TimetableScreen(
    timeTable: TimeTable,
    coursesIdList: List<CourseWithSlotId>,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    title: String,
    targetIndex: Int = 0
) {
    when {
        coursesIdList.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "今天没有课程，是自由的一天！",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        else -> {
            val sortedCourses = remember(coursesIdList) {
                coursesIdList.sortedWith(compareBy { timeTable.toCourseUi(it)!!.timeSlot.startTime })
            }


            ScalingLazyColumn(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = rememberScalingLazyListState(initialCenterItemIndex = targetIndex)
            ) {
                item {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                itemsIndexed(
                    items = sortedCourses,
                    key = { _, item -> "${item.courseId}-${item.timeSlotId}" } // 唯一 key
                ) { dailyOrderIndex, idPair ->
                    val course = timeTable.toCourseUi(idPair)?.copy(dailyOrder = dailyOrderIndex + 1)
                    if (course != null) {
                        TimeTableCard(course) {
                            // 传递两ID
                            navController.navigate("course_detail/${idPair.courseId}/${idPair.timeSlotId}")
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun TimeTableCard(course: CourseUi, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(course.color),// 卡片背景色
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            // 区域 1 时间
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .padding(top = 3.dp),
                horizontalAlignment = Alignment.End, // 右对齐
            ) {
                TextTime(time = course.timeSlot.startTime)
                Spacer(modifier = Modifier.height(2.dp)) // 垂直间距
                TextTime(time = course.timeSlot.endTime)
            }
            // 区域 2 名称
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = course.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center // 水平文字居中
            )
            // 区域 3 节次
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = course.dailyOrder.toString(),
                style = MaterialTheme.typography.displaySmall,
            )
        }
    }
}

@Composable
fun TextTime(time: LocalTime) {
    val context = LocalContext.current
    val is24Hour = remember { DateFormat.is24HourFormat(context) }
    val localTime = remember(time) { java.time.LocalTime.of(time.hour, time.minute) }
    val aTextStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = 10.sp,
        lineHeight = 10.sp
    )
    val timeStr = remember(localTime, is24Hour) {
        if (is24Hour) TimeFormatters.formatter24h.format(localTime)
        else TimeFormatters.formatter12h.format(localTime)
    }

    if (is24Hour) {
        Text(
            text = timeStr,
            style = MaterialTheme.typography.labelSmall
        )
    } else {
        val amPm = remember(localTime) { TimeFormatters.amPmFormatter.format(localTime) }
        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = timeStr,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = amPm,
                style = aTextStyle
            )
        }
    }
}