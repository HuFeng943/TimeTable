package com.hufeng943.timetable.shared.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hufeng943.timetable.shared.room.entities.CourseEntity
import com.hufeng943.timetable.shared.room.entities.TimeSlotEntity
import com.hufeng943.timetable.shared.room.entities.TimeTableEntity
import com.hufeng943.timetable.shared.room.relations.TimeTableWithCourses
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTableDao {
    // 插入
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTable(timeTable: TimeTableEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeSlots(slots: List<TimeSlotEntity>)

    // 删除
    @Delete
    suspend fun deleteTimeTable(timeTable: TimeTableEntity)

    // 查询

    // 获取一个完整的嵌套课程表对象。
    @Transaction
    @Query("SELECT * FROM time_tables WHERE id = :id")
    fun getTimeTableWithCoursesFlow(id: Long): Flow<TimeTableWithCourses?>

}