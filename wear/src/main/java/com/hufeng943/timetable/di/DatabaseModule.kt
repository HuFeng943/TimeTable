package com.hufeng943.timetable.di

import android.content.Context
import androidx.room.Room
import com.hufeng943.timetable.shared.data.dao.TimeTableDao
import com.hufeng943.timetable.shared.data.database.AppDatabase
import com.hufeng943.timetable.shared.data.repository.TimeTableRepository
import com.hufeng943.timetable.shared.data.repository.TimeTableRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "timetable_db"
        ).build()
    }

    @Provides
    fun provideTimeTableDao(db: AppDatabase): TimeTableDao {
        return db.timeTableDao()
    }

    @Provides
    @Singleton
    fun provideRepository(dao: TimeTableDao): TimeTableRepository {
        return TimeTableRepositoryImpl(dao)
    }
}