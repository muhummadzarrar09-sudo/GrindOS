package com.grindos.app.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grindos.app.data.repository.SprintRepository
import com.grindos.app.data.repository.SettingsRepository
import com.grindos.app.domain.model.SprintMode
import com.grindos.app.domain.model.SprintSession
import com.grindos.app.util.XpCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class SprintTimerUiState(
    val selectedMode: SprintMode = SprintMode.FOCUS_SPRINT,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val timeRemainingSeconds: Int = 25 * 60,
    val totalDurationSeconds: Int = 25 * 60,
    val completedSprints: Int = 0
)

@HiltViewModel
class SprintTimerViewModel @Inject constructor(
    private val sprintRepository: SprintRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SprintTimerUiState())
    val uiState: StateFlow<SprintTimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var currentSessionId: Long? = null

    fun selectMode(mode: SprintMode) {
        if (!_uiState.value.isRunning) {
            val duration = mode.durationMinutes * 60
            _uiState.update {
                it.copy(
                    selectedMode = mode,
                    timeRemainingSeconds = duration,
                    totalDurationSeconds = duration
                )
            }
        }
    }

    fun startTimer() {
        if (_uiState.value.isRunning) return

        viewModelScope.launch {
            val session = SprintSession(
                mode = _uiState.value.selectedMode,
                startedAt = LocalDateTime.now()
            )
            currentSessionId = sprintRepository.insertSession(session)
        }

        _uiState.update { it.copy(isRunning = true, isPaused = false) }

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeRemainingSeconds > 0 && _uiState.value.isRunning && !_uiState.value.isPaused) {
                delay(1000L)
                _uiState.update { it.copy(timeRemainingSeconds = it.timeRemainingSeconds - 1) }
            }

            if (_uiState.value.timeRemainingSeconds <= 0) {
                completeSprint()
            }
        }
    }

    fun pauseTimer() {
        _uiState.update { it.copy(isPaused = true) }
    }

    fun resumeTimer() {
        _uiState.update { it.copy(isPaused = false) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeRemainingSeconds > 0 && _uiState.value.isRunning && !_uiState.value.isPaused) {
                delay(1000L)
                _uiState.update { it.copy(timeRemainingSeconds = it.timeRemainingSeconds - 1) }
            }
            if (_uiState.value.timeRemainingSeconds <= 0) {
                completeSprint()
            }
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        val duration = _uiState.value.selectedMode.durationMinutes * 60
        _uiState.update {
            it.copy(
                isRunning = false,
                isPaused = false,
                timeRemainingSeconds = duration,
                totalDurationSeconds = duration
            )
        }
    }

    private suspend fun completeSprint() {
        val xp = XpCalculator.calculateSprintXp(_uiState.value.selectedMode)
        settingsRepository.addXp(xp)

        _uiState.update {
            it.copy(
                isRunning = false,
                isPaused = false,
                completedSprints = it.completedSprints + 1,
                timeRemainingSeconds = 0
            )
        }
    }

    fun getFormattedTime(): String {
        val total = _uiState.value.timeRemainingSeconds
        val minutes = total / 60
        val seconds = total % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
