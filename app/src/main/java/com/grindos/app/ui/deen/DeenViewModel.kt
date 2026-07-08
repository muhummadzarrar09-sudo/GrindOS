package com.grindos.app.ui.deen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grindos.app.data.repository.PrayerRepository
import com.grindos.app.data.repository.QuranRepository
import com.grindos.app.data.repository.SettingsRepository
import com.grindos.app.domain.model.Prayer
import com.grindos.app.domain.model.QuranEntry
import com.grindos.app.util.XpCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DeenUiState(
    val prayers: List<Prayer> = emptyList(),
    val quranEntry: QuranEntry? = null,
    val dailyQuranTarget: Int = 2,
    val isLoading: Boolean = true
)

@HiltViewModel
class DeenViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val quranRepository: QuranRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeenUiState())
    val uiState: StateFlow<DeenUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            prayerRepository.ensureDayPrayersExist(LocalDate.now())
            prayerRepository.getPrayersForDate(LocalDate.now()).collect { prayers ->
                _uiState.update { it.copy(prayers = prayers, isLoading = false) }
            }
        }

        viewModelScope.launch {
            quranRepository.observeEntryForDate(LocalDate.now()).collect { entry ->
                _uiState.update { it.copy(quranEntry = entry) }
            }
        }

        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _uiState.update { it.copy(dailyQuranTarget = settings.dailyQuranPages) }
            }
        }
    }

    fun togglePrayer(prayerId: Long) {
        viewModelScope.launch {
            val prayer = _uiState.value.prayers.find { it.id == prayerId }
            if (prayer != null) {
                prayerRepository.setPrayerCompleted(prayerId, !prayer.completed)
                if (!prayer.completed) {
                    settingsRepository.addXp(XpCalculator.XP_PRAYER_ON_TIME)
                }
            }
        }
    }

    fun updateQuranPages(pagesRead: Int) {
        viewModelScope.launch {
            val current = _uiState.value.quranEntry
            val entry = QuranEntry(
                id = current?.id ?: 0L,
                date = LocalDate.now(),
                targetPages = _uiState.value.dailyQuranTarget,
                pagesRead = pagesRead
            )
            quranRepository.insertOrUpdateEntry(entry)
            if (pagesRead >= _uiState.value.dailyQuranTarget && (current?.pagesRead ?: 0) < _uiState.value.dailyQuranTarget) {
                settingsRepository.addXp(XpCalculator.XP_QURAN_TARGET_MET)
            }
        }
    }
}
