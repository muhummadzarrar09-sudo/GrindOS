package com.grindos.app.data.repository

import com.grindos.app.data.local.dao.StudyTopicDao
import com.grindos.app.data.local.entity.StudyTopicEntity
import com.grindos.app.domain.model.StudySection
import com.grindos.app.domain.model.StudyTopic
import com.grindos.app.domain.model.TopicStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyTopicRepository @Inject constructor(
    private val studyTopicDao: StudyTopicDao
) {
    fun getAllTopics(): Flow<List<StudyTopic>> =
        studyTopicDao.getAllTopics().map { entities -> entities.map { it.toDomain() } }

    fun getTopicsBySection(section: StudySection): Flow<List<StudyTopic>> =
        studyTopicDao.getTopicsBySection(section).map { entities -> entities.map { it.toDomain() } }

    fun getTopicsByStatus(status: TopicStatus): Flow<List<StudyTopic>> =
        studyTopicDao.getTopicsByStatus(status).map { entities -> entities.map { it.toDomain() } }

    fun getMasteredCount(): Flow<Int> = studyTopicDao.getMasteredCount()

    fun getTotalCount(): Flow<Int> = studyTopicDao.getTotalCount()

    suspend fun getTopicById(id: Long): StudyTopic? = studyTopicDao.getTopicById(id)?.toDomain()

    suspend fun insertTopic(topic: StudyTopic): Long = studyTopicDao.insertTopic(topic.toEntity())

    suspend fun updateTopic(topic: StudyTopic) = studyTopicDao.updateTopic(topic.toEntity())

    suspend fun deleteTopic(topic: StudyTopic) = studyTopicDao.deleteTopic(topic.toEntity())

    suspend fun updateTopicProgress(topicId: Long, status: TopicStatus, accuracy: Float?) =
        studyTopicDao.updateTopicProgress(topicId, status, accuracy)

    private fun StudyTopicEntity.toDomain() = StudyTopic(
        id = id, section = section, name = name,
        status = status, accuracy = accuracy, notes = notes
    )

    private fun StudyTopic.toEntity() = StudyTopicEntity(
        id = id, section = section, name = name,
        status = status, accuracy = accuracy, notes = notes
    )
}
