package com.example.expensetracker.data.local.dao

import androidx.room.*
import com.example.expensetracker.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY date DESC")
    fun getUserExpenses(userId: String): Flow<List<ExpenseEntity>>

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
} 