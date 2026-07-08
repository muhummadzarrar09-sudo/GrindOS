package com.grindos.app.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grindos.app.data.repository.StudyTopicRepository
import com.grindos.app.domain.model.StudySection
import com.grindos.app.domain.model.StudyTopic
import com.grindos.app.domain.model.TopicStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudyUiState(
    val topics: List<StudyTopic> = emptyList(),
    val selectedSection: StudySection? = null,
    val masteredCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val studyTopicRepository: StudyTopicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyUiState())
    val uiState: StateFlow<StudyUiState> = _uiState.asStateFlow()

    init {
        loadTopics()
    }

    private fun loadTopics() {
        viewModelScope.launch {
            studyTopicRepository.getAllTopics().collect { topics ->
                _uiState.update {
                    it.copy(
                        topics = topics,
                        totalCount = topics.size,
                        masteredCount = topics.count { t -> t.status == TopicStatus.MASTERED },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectSection(section: StudySection?) {
        _uiState.update { it.copy(selectedSection = section) }
        viewModelScope.launch {
            val flow = if (section != null) {
                studyTopicRepository.getTopicsBySection(section)
            } else {
                studyTopicRepository.getAllTopics()
            }
            flow.collect { topics ->
                _uiState.update { it.copy(topics = topics) }
            }
        }
    }

    fun updateTopicStatus(topic: StudyTopic, newStatus: TopicStatus) {
        viewModelScope.launch {
            studyTopicRepository.updateTopicProgress(topic.id, newStatus, topic.accuracy)
        }
    }
}
