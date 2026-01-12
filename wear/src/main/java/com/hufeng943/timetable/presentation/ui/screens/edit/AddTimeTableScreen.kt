package com.hufeng943.timetable.presentation.ui.screens.edit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.DatePicker
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.contract.TableAction
import com.hufeng943.timetable.shared.model.TimeTable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate


@Composable
fun AddTimeTable(
    existingTables: List<TimeTable>, onAction: (TableAction) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    var pickerType by remember { mutableStateOf<PickerType>(PickerType.Main) }
    var semesterStartDate by remember { mutableStateOf(LocalDate(2023, 1, 1)) }
    var semesterEndDate by remember { mutableStateOf(LocalDate(2023, 5, 1)) }

    Crossfade(targetState = pickerType, label = "ScreenCrossfade") { currentPicker ->
        when (currentPicker) {
            PickerType.Main -> {
                AppScaffold {
                    ScreenScaffold(scrollState = scrollState, edgeButton = {
                        EdgeButton(onClick = { /* 执行保存逻辑 */ }) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = stringResource(R.string.check)
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
                                        stringResource(R.string.edit_timetable_add),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }

                            item {
                                TitleCard(
                                    onClick = { pickerType = PickerType.SemesterName }, // 跳转修改名称
                                    title = { Text(stringResource(R.string.edit_timetable_name)) },
                                ) { Text("哈吉米", style = MaterialTheme.typography.labelLarge) }
                            }

                            item {
                                TitleCard(
                                    onClick = { pickerType = PickerType.StartDate }, // 跳转开始日期
                                    title = { Text(stringResource(R.string.edit_timetable_start)) },
                                ) {
                                    Text(
                                        semesterStartDate.toString(),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }

                            item {
                                TitleCard(
                                    onClick = { pickerType = PickerType.EndDate }, // 跳转结束日期
                                    title = { Text(stringResource(R.string.edit_timetable_end)) },
                                ) {
                                    Text(
                                        semesterEndDate.toString(),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                            item {
                                TitleCard(
                                    onClick = { /* 跳转 */ },
                                    title = { Text(stringResource(R.string.edit_timetable_color)) },
                                ) {
                                    Text("哈吉米", style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                    }
                }
            }

            is PickerType.StartDate, is PickerType.EndDate -> {
                BackHandler {}// 禁止返回

                // 确定当前是给谁选日期
                val isStart = currentPicker is PickerType.StartDate
                val initialDate = if (isStart) semesterStartDate else semesterEndDate

                DatePicker(
                    onDatePicked = { newDate ->
                        val newKotlinDate = newDate.toKotlinLocalDate()
                        if (isStart) semesterStartDate = newKotlinDate else semesterEndDate =
                            newKotlinDate
                        pickerType = PickerType.Main
                    }, initialDate = initialDate.toJavaLocalDate()
                )
            }

            PickerType.SemesterName -> {
                BackHandler { pickerType = PickerType.Main }
                // TODO 名称输入界面
            }

            PickerType.Color -> {
                BackHandler { pickerType = PickerType.Main }
                // TODO 颜色选择界面
            }
        }
    }
}

sealed class PickerType {
    object Main : PickerType()
    object SemesterName : PickerType()
    object StartDate : PickerType()
    object EndDate : PickerType()
    object Color : PickerType()
}