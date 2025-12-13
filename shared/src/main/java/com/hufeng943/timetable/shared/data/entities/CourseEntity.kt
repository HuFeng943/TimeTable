package com.hufeng943.timetable.shared.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "courses",
    foreignKeys = [
        ForeignKey(
            entity = TimeTableEntity::class,
            parentColumns = ["id"],
            childColumns = ["timeTableId"],
            onDelete = ForeignKey.CASCADE
        )// 与TimeTableEntity绑定
    ],
    indices = [
        Index("timeTableId"),
    ]
)
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long= 0,
    val timeTableId: Long,
    val name: String,
    val location: String?,
    val color: Long,
    val teacher: String?
)
