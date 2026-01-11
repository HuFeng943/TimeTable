package com.hufeng943.timetable.presentation.ui.screens.edit

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.DatePicker
import androidx.wear.compose.material3.HorizontalPageIndicator
import com.hufeng943.timetable.presentation.contract.TableAction
import com.hufeng943.timetable.shared.model.TimeTable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate


@Composable
fun AddTimeTable(
    existingTables: List<TimeTable>, onAction: (TableAction) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (page) {
                    0 -> {
                        DatePicker(
                            onDatePicked = { localDate ->
                                Log.v("add", localDate.toKotlinLocalDate().toString())
                            },
                            initialDate = LocalDate(2023, 1, 1)
                                .toJavaLocalDate())
                    }
                }
            }
        }

        HorizontalPageIndicator(
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
