package com.grindos.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grindos.app.data.repository.*
import com.grindos.app.domain.model.*
import com.grindos.app.domain.usecase.CalculateStreakUseCase
import com.grindos.app.domain.usecase.CalculateXpUseCase
import com.grindos.app.domain.usecase.GetPanicSuggestionUseCase
import com.grindos.app.util.XpCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val greeting: String = "Today's Mission",
    val topTasks: List<Task> = emptyList(),
    val prayers: List<Prayer> = emptyList(),
    val completedPrayers: Int = 0,
    val quranEntry: QuranEntry? = null,
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val level: String = "Rookie",
    val levelProgress: Float = 0f,
    val panicMessage: String = "",
    val panicSuggestion: String = "",
    val showPanicDialog: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val prayerRepository: PrayerRepository,
    private val quranRepository: QuranRepository,
    private val settingsRepository: SettingsRepository,
    private val calculateXpUseCase: CalculateXpUseCase,
    private val calculateStreakUseCase: CalculateStreakUseCase,
    private val getPanicSuggestionUseCase: GetPanicSuggestionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            // Initialize prayers for today
            prayerRepository.ensureDayPrayersExist(java.time.LocalDate.now())

            // Load tasks
            taskRepository.getTopPriorityTasks(java.time.LocalDate.now())
                .collect { tasks ->
                    _uiState.update { it.copy(topTasks = tasks) }
                }
        }

        viewModelScope.launch {
            prayerRepository.getPrayersForDate(java.time.LocalDate.now())
                .collect { prayers ->
                    _uiState.update {
                        it.copy(
                            prayers = prayers,
                            completedPrayers = prayers.count { p -> p.completed }
                        )
                    }
                }
        }

        viewModelScope.launch {
            quranRepository.observeEntryForDate(java.time.LocalDate.now())
                .collect { entry ->
                    _uiState.update { it.copy(quranEntry = entry) }
                }
        }

        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _uiState.update {
                    it.copy(
                        totalXp = settings.totalXp,
                        currentStreak = settings.currentStreak,
                        level = calculateXpUseCase.getLevel(settings.totalXp),
                        levelProgress = calculateXpUseCase.getProgress(settings.totalXp),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun toggleTaskComplete(task: Task) {
        viewModelScope.launch {
            taskRepository.setTaskCompleted(task.id, !task.isCompleted)
            if (!task.isCompleted) {
                val xp = if (task.priority == TaskPriority.HIGH) {
                    XpCalculator.XP_TASK_HIGH_PRIORITY
                } else {
                    XpCalculator.XP_TASK_COMPLETE
                }
                settingsRepository.addXp(xp)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun togglePrayer(prayerId: Long) {
        viewModelScope.launch {
            val prayers = _uiState.value.prayers
            val prayer = prayers.find { it.id == prayerId }
            if (prayer != null) {
                prayerRepository.setPrayerCompleted(prayerId, !prayer.completed)
                if (!prayer.completed) {
                    settingsRepository.addXp(XpCalculator.XP_PRAYER_ON_TIME)
                }
            }
        }
    }

    fun triggerPanic() {
        val suggestion = getPanicSuggestionUseCase()
        _uiState.update {
            it.copy(
                showPanicDialog = true,
                panicMessage = suggestion.calmingMessage,
                panicSuggestion = suggestion.suggestedTask
            )
        }
    }

    fun dismissPanic() {
        _uiState.update { it.copy(showPanicDialog = false) }
    }
}
