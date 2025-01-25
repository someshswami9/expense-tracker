package com.example.expensetracker.data.model

data class NotificationPreferences(
    val expenseWarnings: Boolean = true,
    val budgetWarnings: Boolean = true,
    val dailyRecap: Boolean = false,
    val monthlyRecap: Boolean = true,
    val customCategoryLimits: Map<ExpenseCategory, Double> = emptyMap()
) 