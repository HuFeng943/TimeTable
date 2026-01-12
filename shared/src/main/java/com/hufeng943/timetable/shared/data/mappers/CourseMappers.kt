package com.hufeng943.timetable.shared.data.mappers

import com.hufeng943.timetable.shared.data.entities.CourseEntity
import com.hufeng943.timetable.shared.data.relations.CourseWithSlots
import com.hufeng943.timetable.shared.model.Course

fun CourseWithSlots.toCourse(): Course {
    return Course(
        id = this.course.id,
        name = this.course.name,
        location = this.course.location,
        color = this.course.color,
        teacher = this.course.teacher,
        timeSlots = this.timeSlots.map { it.toTimeSlot() }
    )
}

fun Course.toCourseEntity(timetableId: Long): CourseEntity {
    return CourseEntity(
        id = this.id,
        timetableId = timetableId, // 关联到父课表
        name = this.name,
        location = this.location,
        color = this.color,
        teacher = this.teacher
    )
}