package com.hufeng943.timetable.shared.data.mappers

import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.data.relations.CourseWithSlots

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