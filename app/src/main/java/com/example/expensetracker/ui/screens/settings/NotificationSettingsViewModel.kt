package com.example.expensetracker.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.model.NotificationPreferences
import com.example.expensetracker.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val preferences: StateFlow<NotificationPreferences> = preferencesRepository
        .notificationPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationPreferences()
        )

    fun updateExpenseWarnings(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateExpenseWarnings(enabled)
        }
    }

    fun updateBudgetWarnings(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateBudgetWarnings(enabled)
        }
    }

    fun updateDailyRecap(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateDailyRecap(enabled)
        }
    }

    fun updateMonthlyRecap(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateMonthlyRecap(enabled)
        }
    }
} 