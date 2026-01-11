package com.hufeng943.timetable.presentation.ui

object NavRoutes {
    const val LOADING = "loading"
    const val MAIN = "main"
    const val EDIT_COURSE = "edit_course"
    const val ADD_COURSE = "add_course"
    const val COURSE_DETAIL = "course_detail/{courseId}/{timeSlotId}"

    const val ERROR = "error"
    fun courseDetail(courseId: Long, timeSlotId: Long) = "course_detail/$courseId/$timeSlotId"
    // TODO "course_detail/{timeTableId}/{courseId}/{timeSlotId}"
}