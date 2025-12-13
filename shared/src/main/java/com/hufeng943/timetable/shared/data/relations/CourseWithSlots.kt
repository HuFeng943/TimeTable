package com.hufeng943.timetable.shared.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hufeng943.timetable.shared.data.entities.CourseEntity
import com.hufeng943.timetable.shared.data.entities.TimeSlotEntity

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