package com.example.expensetracker.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.service.NotificationService
import com.example.expensetracker.data.repository.PreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class NotificationHelper @Inject constructor(
    private val context: Context,
    private val notificationService: NotificationService,
    private val preferencesRepository: PreferencesRepository
) {
    fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun showExpenseWarning(expense: Expense, categoryLimit: Double) {
        if (!checkNotificationPermission()) return
        
        // Check if expense warnings are enabled
        preferencesRepository.notificationPreferences.first().let { prefs ->
            if (!prefs.expenseWarnings) return
        }
        
        if (expense.amount > categoryLimit && expense.category.name != "RENT") {
            notificationService.showNotification(
                title = "High Expense Warning",
                message = "Your ${expense.category.name.lowercase()} expense of " +
                        "${expense.amount} exceeds the limit of $categoryLimit"
            )
        }
    }

    fun showBudgetWarning(totalExpenses: Double, monthlyBudget: Double) {
        if (!checkNotificationPermission()) return
        
        // Check if budget warnings are enabled
        preferencesRepository.notificationPreferences.first().let { prefs ->
            if (!prefs.budgetWarnings) return
        }
        
        if (totalExpenses > monthlyBudget) {
            notificationService.showNotification(
                title = "Budget Warning",
                message = "You have exceeded your monthly budget of $monthlyBudget"
            )
        }
    }
} 