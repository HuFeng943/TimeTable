package com.hufeng943.timetable.shared.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hufeng943.timetable.shared.room.dao.TimeTableDao
import com.hufeng943.timetable.shared.room.entities.CourseEntity
import com.hufeng943.timetable.shared.room.entities.TimeSlotEntity
import com.hufeng943.timetable.shared.room.entities.TimeTableEntity

@Database(
    entities = [
        TimeTableEntity::class,
        CourseEntity::class,
        TimeSlotEntity::class
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timeTableDao(): TimeTableDao
}