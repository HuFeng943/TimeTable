// 文件名: ./ui/mappers/TimeTableLookup.kt
package com.hufeng943.timetable.shared.ui.mappers

import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseUi
import com.hufeng943.timetable.shared.ui.CourseWithSlotId

// 用timeSlotId查对应TimeSlot
fun TimeTable.findTimeSlotById(timeSlotId: Long): TimeSlot? {
    // 依赖于 TimeTable.allTimeSlotsMap
    return this.allTimeSlotsMap[timeSlotId]
}
// 用CourseWithSlotId反查CourseUi
fun TimeTable.toCourseUi(
    courseWithSlotId: CourseWithSlotId,
): CourseUi? {
    val course = this.courseMap[courseWithSlotId.courseID]
    val timeSlot = this.allTimeSlotsMap[courseWithSlotId.timeSlotID]

    // 没找到？
    if (course == null || timeSlot == null) return null

    return CourseUi(
        name = course.name,
        timeSlot = timeSlot,
        color = course.color,
        location = course.location,
        teacher = course.teacher
    )
}