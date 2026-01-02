package com.hufeng943.timetable.presentation.ui.components

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
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
fun TextTime(time: LocalTime) {
    val context = LocalContext.current
    val is24Hour = DateFormat.is24HourFormat(context)
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