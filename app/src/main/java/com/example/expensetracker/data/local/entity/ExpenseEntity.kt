package com.example.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetracker.data.model.ExpenseCategory
import java.util.Date

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val amount: Double,
    val category: ExpenseCategory,
    val date: Date,
    val description: String
) 