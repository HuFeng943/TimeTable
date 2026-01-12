package com.hufeng943.timetable.shared.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hufeng943.timetable.shared.data.entities.CourseEntity
import com.hufeng943.timetable.shared.data.entities.TimetableEntity

data class TimetableWithCourses(
    @Embedded val timetable: TimetableEntity,
    @Relation(
        // 基于 CourseEntity 的联系
        entity = CourseEntity::class,
        // TimetableEntity.id
        parentColumn = "id",
        // CourseEntity.timetableId
        entityColumn = "timetableId"
    )
    val courses: List<CourseWithSlots> // 列表类型是内层 Relation Class
)