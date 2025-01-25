package com.example.expensetracker.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.expensetracker.data.model.ExpenseCategory
import com.example.expensetracker.data.model.NotificationPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val EXPENSE_WARNINGS = booleanPreferencesKey("expense_warnings")
        val BUDGET_WARNINGS = booleanPreferencesKey("budget_warnings")
        val DAILY_RECAP = booleanPreferencesKey("daily_recap")
        val MONTHLY_RECAP = booleanPreferencesKey("monthly_recap")
    }

    val notificationPreferences: Flow<NotificationPreferences> = context.dataStore.data
        .map { preferences ->
            NotificationPreferences(
                expenseWarnings = preferences[PreferencesKeys.EXPENSE_WARNINGS] ?: true,
                budgetWarnings = preferences[PreferencesKeys.BUDGET_WARNINGS] ?: true,
                dailyRecap = preferences[PreferencesKeys.DAILY_RECAP] ?: false,
                monthlyRecap = preferences[PreferencesKeys.MONTHLY_RECAP] ?: true
            )
        }

    suspend fun updateExpenseWarnings(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.EXPENSE_WARNINGS] = enabled
        }
    }

    suspend fun updateBudgetWarnings(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BUDGET_WARNINGS] = enabled
        }
    }

    suspend fun updateDailyRecap(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_RECAP] = enabled
        }
    }

    suspend fun updateMonthlyRecap(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.MONTHLY_RECAP] = enabled
        }
    }
} 