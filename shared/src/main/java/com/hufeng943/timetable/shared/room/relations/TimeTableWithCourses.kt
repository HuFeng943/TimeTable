package com.hufeng943.timetable.shared.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hufeng943.timetable.shared.room.entities.CourseEntity
import com.hufeng943.timetable.shared.room.entities.TimeTableEntity

data class TimeTableWithCourses(
    @Embedded val timeTable: TimeTableEntity,
    @Relation(
        // 基于 CourseEntity 的联系
        entity = CourseEntity::class,
        // TimeTableEntity.id
        parentColumn = "id",
        // CourseEntity.timeTableId
        entityColumn = "timeTableId"
    )
    val courses: List<CourseWithSlots> // 列表类型是内层 Relation Class
)