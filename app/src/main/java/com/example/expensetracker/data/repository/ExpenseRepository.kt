package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.dao.ExpenseDao
import com.example.expensetracker.data.local.entity.ExpenseEntity
import com.example.expensetracker.data.model.Expense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import com.example.expensetracker.util.NotificationHelper

@Singleton
class ExpenseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val expenseDao: ExpenseDao,
    private val notificationHelper: NotificationHelper
) {
    private val userId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

    companion object {
        private const val CATEGORY_LIMIT = 1500.0 // Default category limit
    }

    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getUserExpenses(userId).map { entities ->
            entities.map { it.toExpense() }
        }
    }

    suspend fun addExpense(expense: Expense) {
        // Check for expense limits
        notificationHelper.showExpenseWarning(expense, CATEGORY_LIMIT)
        
        // Save to Firestore
        try {
            firestore.collection("expenses")
                .document(expense.id)
                .set(expense)
                .await()
        } catch (e: Exception) {
            // Handle Firestore error
        }

        // Save to local database
        expenseDao.insertExpense(expense.toEntity())
    }

    private fun ExpenseEntity.toExpense() = Expense(
        id = id,
        userId = userId,
        amount = amount,
        category = category,
        date = date,
        description = description
    )

    private fun Expense.toEntity() = ExpenseEntity(
        id = id,
        userId = userId,
        amount = amount,
        category = category,
        date = date,
        description = description
    )
} 