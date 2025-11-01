package com.hufeng943.timetable.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text

import com.hufeng943.timetable.shared.*
import com.hufeng943.timetable.presentation.util.TextTime

@Composable
fun TimetableScreen(
    modifier: Modifier = Modifier,
    courses: List<Pair<TimeSlot, Course>>,
    title: String,
    targetIndex: Int = 0
) {

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
        if (courses.isEmpty()) {
            item {
                Text(
                    "今天没课，休息！",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {

            items(courses.size) { index ->
                val (slot: TimeSlot, course: Course) = courses[index]
                Card(
                    onClick = {}
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        // 区域 1 时间
                        Column(
                            modifier = Modifier.width(IntrinsicSize.Min),
                            horizontalAlignment = Alignment.End, // 右对齐
                            verticalArrangement = Arrangement.Top
                        ) {
                            TextTime(time = slot.startTime)
                            Spacer(modifier = Modifier.height(2.dp)) // 垂直间距
                            TextTime(time = slot.endTime)
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(course.name, style = MaterialTheme.typography.titleMedium)
                    }

                }
            }
        }
    }
}