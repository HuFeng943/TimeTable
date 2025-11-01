package com.hufeng943.timetable.presentation.util

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import kotlinx.datetime.LocalTime
import java.util.Locale


@Composable
fun TextTime(time: LocalTime){
    val context = LocalContext.current
    val localDateTime = java.time.LocalTime.of(time.hour, time.minute)
    val formatter1 = java.time.format.DateTimeFormatter
        .ofPattern("HH:mm", Locale.getDefault())

    if (DateFormat.is24HourFormat(context)) {
        Text(
            text = formatter1.format(localDateTime),
            style = MaterialTheme.typography.labelMedium)
    }else{
        val formatter2 = java.time.format.DateTimeFormatter
            .ofPattern("a", Locale.getDefault())
        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.End, // 右对齐
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formatter1.format(localDateTime),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = formatter2.format(localDateTime),
                style = MaterialTheme.typography.titleSmall
            )
        }

    }
}