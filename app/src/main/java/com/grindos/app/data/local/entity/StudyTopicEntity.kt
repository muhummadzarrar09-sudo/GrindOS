package com.grindos.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grindos.app.domain.model.StudySection
import com.grindos.app.domain.model.TopicStatus

@Entity(tableName = "study_topics")
data class StudyTopicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val section: StudySection,
    val name: String,
    val status: TopicStatus = TopicStatus.NOT_STARTED,
    val accuracy: Float? = null,
    val notes: String? = null
)
