package com.example.expensetracker.data.model

import java.util.Date

data class Expense(
    val id: String = "",
    val userId: String = "",
    val amount: Double = 0.0,
    val category: ExpenseCategory = ExpenseCategory.OTHER,
    val date: Date = Date(),
    val description: String = ""
)

enum class ExpenseCategory {
    FOOD,
    RENT,
    TRANSPORTATION,
    UTILITIES,
    ENTERTAINMENT,
    SHOPPING,
    HEALTH,
    OTHER
} 