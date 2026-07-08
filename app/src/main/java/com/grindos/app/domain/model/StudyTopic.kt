package com.grindos.app.domain.model

enum class StudySection {
    BASIC_MATH, ADVANCED_MATH, IQ, ANALYTICAL, ENGLISH, MIXED_MOCKS
}

enum class TopicStatus {
    NOT_STARTED, LEARNING, PRACTICING, TIMED, MASTERED
}

data class StudyTopic(
    val id: Long = 0L,
    val section: StudySection,
    val name: String,
    val status: TopicStatus = TopicStatus.NOT_STARTED,
    val accuracy: Float? = null,
    val notes: String? = null
)
