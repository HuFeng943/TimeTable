package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.contract.TableAction
import com.hufeng943.timetable.shared.data.repository.TimeTableRepository
import com.hufeng943.timetable.shared.model.TimeTable
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TimeTableViewModel @Inject constructor(
    private val repository: TimeTableRepository
) : ViewModel() {

    // Flow -> Compose state
    val timeTables: StateFlow<List<TimeTable>?> =
        repository.getAllTimeTables().stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onAction(action: TableAction) {
        when (action) {
            is TableAction.Add -> addTimeTable(action.table)
        }
    }
    fun addTimeTable(newTable: TimeTable) {
        viewModelScope.launch {
            repository.insertTimeTable(newTable)
        }
    }

}
