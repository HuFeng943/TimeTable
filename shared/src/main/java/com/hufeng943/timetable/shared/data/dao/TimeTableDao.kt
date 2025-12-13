package com.hufeng943.timetable.shared.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hufeng943.timetable.shared.data.entities.CourseEntity
import com.hufeng943.timetable.shared.data.entities.TimeSlotEntity
import com.hufeng943.timetable.shared.data.entities.TimeTableEntity
import com.hufeng943.timetable.shared.data.relations.TimeTableWithCourses
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTableDao {
    // ------插入------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTable(timeTable: TimeTableEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeSlots(slots: List<TimeSlotEntity>)

    // ------删除------
    @Delete
    suspend fun deleteTimeTable(timeTable: TimeTableEntity)

    // 按 ID 删除
    @Query("DELETE FROM time_tables WHERE id = :id")
    suspend fun deleteTimeTableById(id: Long)

    // ------查询-------

    // 获取一个完整的嵌套课程表对象。
    @Transaction
    @Query("SELECT * FROM time_tables WHERE id = :id")
    fun getTimeTableWithCoursesFlow(id: Long): Flow<TimeTableWithCourses?>

    // 查询所有课表
    @Transaction
    @Query("SELECT * FROM time_tables")
    fun getAllTimeTablesWithCourses(): Flow<List<TimeTableWithCourses>>
}