package com.grindos.app.di

import android.content.Context
import androidx.room.Room
import com.grindos.app.data.local.GrindOsDatabase
import com.grindos.app.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GrindOsDatabase {
        return Room.databaseBuilder(
            context,
            GrindOsDatabase::class.java,
            GrindOsDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideTaskDao(db: GrindOsDatabase): TaskDao = db.taskDao()

    @Provides
    fun providePrayerDao(db: GrindOsDatabase): PrayerDao = db.prayerDao()

    @Provides
    fun provideQuranEntryDao(db: GrindOsDatabase): QuranEntryDao = db.quranEntryDao()

    @Provides
    fun provideStudyTopicDao(db: GrindOsDatabase): StudyTopicDao = db.studyTopicDao()

    @Provides
    fun provideErrorLogDao(db: GrindOsDatabase): ErrorLogDao = db.errorLogDao()

    @Provides
    fun provideSprintSessionDao(db: GrindOsDatabase): SprintSessionDao = db.sprintSessionDao()

    @Provides
    fun provideHackathonTaskDao(db: GrindOsDatabase): HackathonTaskDao = db.hackathonTaskDao()

    @Provides
    fun provideBookEntryDao(db: GrindOsDatabase): BookEntryDao = db.bookEntryDao()

    @Provides
    fun provideGolfEntryDao(db: GrindOsDatabase): GolfEntryDao = db.golfEntryDao()
}
