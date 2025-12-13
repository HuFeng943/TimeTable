package com.hufeng943.timetable.shared.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hufeng943.timetable.shared.room.entities.CourseEntity
import com.hufeng943.timetable.shared.room.entities.TimeSlotEntity

data class CourseWithSlots(
    @Embedded val course: CourseEntity,
    @Relation(
        // CourseEntity.id
        parentColumn = "id",
        // TimeSlotEntity.courseId
        entityColumn = "courseId"
    )
    val timeSlots: List<TimeSlotEntity>
)