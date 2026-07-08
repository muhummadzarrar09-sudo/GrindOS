package com.grindos.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grindos.app.data.repository.SettingsRepository
import com.grindos.app.domain.model.AppSettings
import com.grindos.app.domain.model.NotificationTone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _uiState.update { it.copy(settings = settings, isLoading = false) }
            }
        }
    }

    fun setNotificationMode(mode: NotificationTone) {
        viewModelScope.launch { settingsRepository.setNotificationMode(mode) }
    }

    fun setDefaultReminderMinutes(minutes: Int) {
        viewModelScope.launch { settingsRepository.setDefaultReminderMinutes(minutes) }
    }

    fun setDailyStudyTarget(minutes: Int) {
        viewModelScope.launch { settingsRepository.setDailyStudyTargetMinutes(minutes) }
    }

    fun setDailyQuranPages(pages: Int) {
        viewModelScope.launch { settingsRepository.setDailyQuranPages(pages) }
    }

    fun setDailyBookPages(pages: Int) {
        viewModelScope.launch { settingsRepository.setDailyBookPages(pages) }
    }
}
