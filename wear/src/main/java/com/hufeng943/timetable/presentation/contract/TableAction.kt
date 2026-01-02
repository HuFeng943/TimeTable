package com.hufeng943.timetable.presentation.contract

import com.hufeng943.timetable.shared.model.TimeTable

sealed class TableAction {
    data class Add(val table: TimeTable) : TableAction()
    // TODO ......
}