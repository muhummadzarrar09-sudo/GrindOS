package com.grindos.app.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grindos.app.data.repository.ErrorLogRepository
import com.grindos.app.domain.model.ErrorLog
import com.grindos.app.domain.model.ErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ErrorLogUiState(
    val errorLogs: List<ErrorLog> = emptyList(),
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false
)

@HiltViewModel
class ErrorLogViewModel @Inject constructor(
    private val errorLogRepository: ErrorLogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ErrorLogUiState())
    val uiState: StateFlow<ErrorLogUiState> = _uiState.asStateFlow()

    init {
        loadErrorLogs()
    }

    private fun loadErrorLogs() {
        viewModelScope.launch {
            errorLogRepository.getAllErrorLogs().collect { logs ->
                _uiState.update { it.copy(errorLogs = logs, isLoading = false) }
            }
        }
    }

    fun addErrorLog(errorLog: ErrorLog) {
        viewModelScope.launch {
            errorLogRepository.insertErrorLog(errorLog)
            _uiState.update { it.copy(showAddDialog = false) }
        }
    }

    fun markFixed(errorLog: ErrorLog) {
        viewModelScope.launch {
            errorLogRepository.updateStatus(errorLog.id, ErrorStatus.FIXED)
        }
    }

    fun deleteErrorLog(errorLog: ErrorLog) {
        viewModelScope.launch {
            errorLogRepository.deleteErrorLog(errorLog)
        }
    }

    fun toggleAddDialog() {
        _uiState.update { it.copy(showAddDialog = !it.showAddDialog) }
    }
}
