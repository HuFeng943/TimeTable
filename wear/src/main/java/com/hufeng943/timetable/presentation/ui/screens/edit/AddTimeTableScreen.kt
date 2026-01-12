package com.hufeng943.timetable.presentation.ui.screens.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.presentation.contract.TableAction
import com.hufeng943.timetable.shared.model.TimeTable


@Composable
fun AddTimeTable(
    existingTables: List<TimeTable>, onAction: (TableAction) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    AppScaffold {
        ScreenScaffold(scrollState = scrollState, edgeButton = {
            EdgeButton(
                onClick = { /* 执行保存逻辑 */ }) {
                Icon(
                    imageVector = Icons.Default.Check, contentDescription = "确定"
                )
            }
        }) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = scrollState,
            ) {
                item {
                    ListHeader {
                        Text(
                            text = "title", style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                item {
                    TitleCard(
                        onClick = { /* 跳转 */ },
                        title = { Text("课程表名称") },
                    ) {
                        Text(
                            "哈吉米", style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                item {
                    TitleCard(
                        onClick = { /* 跳转 */ },
                        title = { Text("学期开始日期") },
                    ) {
                        Text(
                            "哈吉米", style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                item {
                    TitleCard(
                        onClick = { /* 跳转 */ },
                        title = { Text("学期结束日期") },
                    ) {
                        Text(
                            "哈吉米", style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                item {
                    TitleCard(
                        onClick = { /* 跳转 */ },
                        title = { Text("颜色") },
                    ) {
                        Text(
                            "哈吉米", style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

//DatePicker(
//onDatePicked = { localDate ->
//    Log.v("add", localDate.toKotlinLocalDate().toString())
//},
//initialDate = LocalDate(2023, 1, 1)
//.toJavaLocalDate())