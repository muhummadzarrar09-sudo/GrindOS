package com.grindos.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grindos.app.data.local.converter.Converters
import com.grindos.app.data.local.dao.*
import com.grindos.app.data.local.entity.*

@Database(
    entities = [
        TaskEntity::class,
        PrayerEntity::class,
        QuranEntryEntity::class,
        StudyTopicEntity::class,
        ErrorLogEntity::class,
        SprintSessionEntity::class,
        HackathonTaskEntity::class,
        BookEntryEntity::class,
        GolfEntryEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class GrindOsDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun prayerDao(): PrayerDao
    abstract fun quranEntryDao(): QuranEntryDao
    abstract fun studyTopicDao(): StudyTopicDao
    abstract fun errorLogDao(): ErrorLogDao
    abstract fun sprintSessionDao(): SprintSessionDao
    abstract fun hackathonTaskDao(): HackathonTaskDao
    abstract fun bookEntryDao(): BookEntryDao
    abstract fun golfEntryDao(): GolfEntryDao

    companion object {
        const val DATABASE_NAME = "grindos_database"
    }
}
