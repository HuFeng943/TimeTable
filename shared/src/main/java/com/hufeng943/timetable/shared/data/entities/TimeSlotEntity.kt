package com.hufeng943.timetable.shared.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * WeekPattern 枚举值,ordinal (Int),含义
 * EVERY_WEEK,0,每周重复
 * ODD_WEEK,1,单周重复
 * EVEN_WEEK,2,双周重复
 */
@Entity(
    tableName = "time_slots",
    foreignKeys = [
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )// 与CourseEntity绑定
    ],
    indices = [Index("courseId")]
)
data class TimeSlotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val courseId: Long,
    val dayOfWeek: Int,
    /* (start|end)Minute是由LocalTime类型的(start|end)Time
    就是当天0点到(start|end)Time的分钟数
     */
    val startMinute: Int,
    val endMinute: Int,
    val recurrence: Int,
    val remark: String?
)