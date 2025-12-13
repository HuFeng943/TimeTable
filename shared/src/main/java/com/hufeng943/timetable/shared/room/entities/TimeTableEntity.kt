package com.hufeng943.timetable.shared.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_tables")
data class TimeTableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val semesterName: String,
    val createdAtMillis: Long,
    val semesterStartEpochDay: Long,
    val semesterEndEpochDay: Long?
)