package com.grindos.app.ui.life

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grindos.app.data.repository.BookRepository
import com.grindos.app.data.repository.GolfRepository
import com.grindos.app.data.repository.HackathonRepository
import com.grindos.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LifeUiState(
    val hackathonTasks: List<HackathonTask> = emptyList(),
    val bookEntries: List<BookEntry> = emptyList(),
    val golfEntries: List<GolfEntry> = emptyList()
)

@HiltViewModel
class LifeViewModel @Inject constructor(
    private val hackathonRepository: HackathonRepository,
    private val bookRepository: BookRepository,
    private val golfRepository: GolfRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LifeUiState())
    val uiState: StateFlow<LifeUiState> = _uiState.asStateFlow()

    init {
        loadHackathonTasks()
        loadBookEntries()
        loadGolfEntries()
    }

    private fun loadHackathonTasks() {
        viewModelScope.launch {
            hackathonRepository.getAllTasks().collect { tasks ->
                _uiState.update { it.copy(hackathonTasks = tasks) }
            }
        }
    }

    private fun loadBookEntries() {
        viewModelScope.launch {
            bookRepository.getAllEntries().collect { entries ->
                _uiState.update { it.copy(bookEntries = entries) }
            }
        }
    }

    private fun loadGolfEntries() {
        viewModelScope.launch {
            golfRepository.getAllEntries().collect { entries ->
                _uiState.update { it.copy(golfEntries = entries) }
            }
        }
    }

    fun addHackathonTask(title: String) {
        viewModelScope.launch {
            hackathonRepository.insertTask(HackathonTask(title = title))
        }
    }

    fun updateHackathonStatus(taskId: Long, status: KanbanStatus) {
        viewModelScope.launch {
            hackathonRepository.updateTaskStatus(taskId, status)
        }
    }

    fun deleteHackathonTask(task: HackathonTask) {
        viewModelScope.launch {
            hackathonRepository.deleteTask(task)
        }
    }

    fun addBookEntry(bookName: String, pagesRead: Int) {
        viewModelScope.launch {
            bookRepository.insertEntry(BookEntry(bookName = bookName, pagesReadToday = pagesRead))
        }
    }

    fun addGolfEntry(drillName: String, duration: Int) {
        viewModelScope.launch {
            golfRepository.insertEntry(GolfEntry(drillName = drillName, durationMinutes = duration))
        }
    }
}
