package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text

@Composable
fun MorePager() {
    val scrollState = rememberScalingLazyListState()

    ScreenScaffold(scrollState = scrollState) {
        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                ListHeader {
                    Text("设置")
                }
            }

            item {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(),
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Text("编辑课程表")
                }
            }
        }
    }
}