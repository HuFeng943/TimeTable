package com.hufeng943.timetable.shared.data.repository

import com.hufeng943.timetable.shared.data.dao.TimetableDao
import com.hufeng943.timetable.shared.data.mappers.toCourseEntity
import com.hufeng943.timetable.shared.data.mappers.toTimeSlotEntity
import com.hufeng943.timetable.shared.data.mappers.toTimetable
import com.hufeng943.timetable.shared.data.mappers.toTimetableEntity
import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.Timetable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimetableRepositoryImpl(private val dao: TimetableDao) : TimetableRepository {
    override suspend fun insertTimetable(timetable: Timetable): Long {
        val timetableEntity = timetable.toTimetableEntity()
        return dao.insertTimetable(timetableEntity)
    }

    override suspend fun insertCourse(course: Course, timetableId: Long): Long {
        val courseEntity = course.toCourseEntity(timetableId)
        return dao.insertCourse(courseEntity)
    }

    override suspend fun insertTimeSlot(courseId: Long, timeSlot: TimeSlot): Long {
        val timeSlotEntity = timeSlot.toTimeSlotEntity(courseId)
        return dao.insertTimeSlot(timeSlotEntity)
    }

    override suspend fun deleteTimetable(timetableId: Long) {
        dao.deleteTimetableById(timetableId)
    }

    override suspend fun deleteCourse(courseId: Long) {
        dao.deleteCourseById(courseId)
    }

    override suspend fun deleteTimeSlot(timeSlotId: Long) {
        dao.deleteTimeSlotById(timeSlotId)
    }

    override fun getAllTimetables(): Flow<List<Timetable>> {
        return dao.getAllTimetablesWithCourses()
            .map { relationsList ->
                relationsList.map { relation ->
                    relation.toTimetable()
                }
            }
    }
}