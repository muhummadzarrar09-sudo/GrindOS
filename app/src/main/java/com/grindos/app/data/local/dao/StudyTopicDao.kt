package com.grindos.app.data.local.dao

import androidx.room.*
import com.grindos.app.data.local.entity.StudyTopicEntity
import com.grindos.app.domain.model.StudySection
import com.grindos.app.domain.model.TopicStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyTopicDao {

    @Query("SELECT * FROM study_topics ORDER BY section ASC, id ASC")
    fun getAllTopics(): Flow<List<StudyTopicEntity>>

    @Query("SELECT * FROM study_topics WHERE section = :section ORDER BY id ASC")
    fun getTopicsBySection(section: StudySection): Flow<List<StudyTopicEntity>>

    @Query("SELECT * FROM study_topics WHERE id = :id")
    suspend fun getTopicById(id: Long): StudyTopicEntity?

    @Query("SELECT * FROM study_topics WHERE status = :status")
    fun getTopicsByStatus(status: TopicStatus): Flow<List<StudyTopicEntity>>

    @Query("SELECT COUNT(*) FROM study_topics WHERE status = 'MASTERED'")
    fun getMasteredCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM study_topics")
    fun getTotalCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: StudyTopicEntity): Long

    @Update
    suspend fun updateTopic(topic: StudyTopicEntity)

    @Delete
    suspend fun deleteTopic(topic: StudyTopicEntity)

    @Query("UPDATE study_topics SET status = :status, accuracy = :accuracy WHERE id = :topicId")
    suspend fun updateTopicProgress(topicId: Long, status: TopicStatus, accuracy: Float?)
}
