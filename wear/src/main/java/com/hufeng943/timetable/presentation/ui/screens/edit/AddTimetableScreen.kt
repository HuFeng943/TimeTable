package com.hufeng943.timetable.presentation.ui.screens.edit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import com.hufeng943.timetable.shared.model.Timetable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.todayIn
import kotlin.time.Clock


@Suppress("AssignedValueIsNeverRead")
@Composable
fun AddTimetable(
    existingTables: List<Timetable>, onAction: (TableAction) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    var pickerType by remember { mutableStateOf<PickerType>(PickerType.Main) }

    val toDay = Clock.System.todayIn(TimeZone.currentSystemDefault())
    var semesterStartDate by remember { mutableStateOf(toDay) }
    var semesterEndDate: LocalDate? by remember { mutableStateOf(null) }
    var semesterName by remember { mutableStateOf("哈吉米") }

    Crossfade(
        targetState = pickerType, animationSpec = tween(durationMillis = 350)
    ) { currentPicker ->
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

                            item {// 修改名称
                                TitleCard(
                                    onClick = { pickerType = PickerType.SemesterName },
                                    title = {
                                        Text(
                                            stringResource(R.string.edit_timetable_name),
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    },
                                ) {
                                    Text(
                                        semesterName, style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }

                            item {// 开始日期
                                TitleCard(
                                    onClick = { pickerType = PickerType.StartDate },
                                    onLongClick = {
                                        semesterStartDate = toDay
                                        semesterEndDate?.let { end ->
                                            // 如果新修改的开始日期大于结束日期，则重置结束日期为新的开始日期
                                            if (end < semesterStartDate) {
                                                semesterEndDate = semesterStartDate
                                            }
                                        }
                                    },
                                    subtitle = { if (semesterStartDate != toDay) Text("长按设置为当前日期") },
                                    title = { Text(stringResource(R.string.edit_timetable_start)) },
                                ) {
                                    Text(
                                        semesterStartDate.toString(),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }

                            item {// 结束日期
                                TitleCard(
                                    onClick = { pickerType = PickerType.EndDate },
                                    onLongClick = { semesterEndDate = null },
                                    subtitle = { if (semesterEndDate != null) Text("长按设置为永不结束") },
                                    title = { Text(stringResource(R.string.edit_timetable_end)) },
                                ) {
                                    Text(
                                        semesterEndDate?.toString() ?: "永不结束",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                            item {// 颜色
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

            PickerType.StartDate -> {
                BackHandler {}// 禁止返回
                DatePicker(
                    onDatePicked = { newDate ->
                        semesterStartDate = newDate.toKotlinLocalDate()
                        semesterEndDate?.let { end ->// 如果新修改的开始日期大于结束日期，则重置结束日期为新的开始日期
                            if (end < semesterStartDate) {
                                semesterEndDate = semesterStartDate
                            }
                        }
                        pickerType = PickerType.Main
                    },
                    initialDate = semesterStartDate.toJavaLocalDate(),
                )
            }

            PickerType.EndDate -> {
                BackHandler {}// 禁止返回
                DatePicker(
                    onDatePicked = { newDate ->
                        semesterEndDate = newDate.toKotlinLocalDate()
                        pickerType = PickerType.Main
                    },
                    initialDate = (semesterEndDate ?: semesterStartDate).toJavaLocalDate(),
                    minValidDate = semesterStartDate.toJavaLocalDate()
                )
            }

            PickerType.SemesterName -> {
                BackHandler {}// 禁止返回
                val scrollState = rememberScalingLazyListState()
                // 状态管理
                var textValue by remember { mutableStateOf(semesterName) }
                ScreenScaffold(scrollState = scrollState, edgeButton = {
                    EdgeButton(onClick = {
                        semesterName = textValue
                        pickerType = PickerType.Main
                    }) {
                        Icon(
                            Icons.Default.Check, contentDescription = stringResource(R.string.check)
                        )
                    }
                }) {
                    ScalingLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = scrollState
                    ) {
                        item {
                            ListHeader {
                                Text(
                                    text = "输入课表名称",
                                )
                            }
                        }
                        item {
                            BasicTextField(
                                value = textValue,
                                onValueChange = {
                                    // 过滤掉换行符
                                    textValue = it.replace("\n", "")
                                },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceContainer,
                                        shape = CircleShape // 圆角两边
                                    )
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
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
