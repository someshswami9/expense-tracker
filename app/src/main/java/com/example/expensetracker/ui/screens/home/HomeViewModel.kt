package com.example.expensetracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.data.model.User
import com.example.expensetracker.data.repository.AuthRepository
import com.example.expensetracker.data.repository.ExpenseRepository
import com.example.expensetracker.data.repository.UserRepository
import com.example.expensetracker.service.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val expenseRepository: ExpenseRepository,
    private val userRepository: UserRepository,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                authRepository.currentUser,
                expenseRepository.getAllExpenses()
            ) { user, expenses ->
                if (user == null) {
                    HomeUiState.NotAuthenticated
                } else {
                    // Check budget warnings
                    val totalExpenses = expenses.sumOf { it.amount }
                    if (user.monthlyBudget > 0) {
                        notificationHelper.showBudgetWarning(totalExpenses, user.monthlyBudget)
                    }
                    
                    HomeUiState.Success(
                        user = user,
                        expenses = expenses,
                        expensesByCategory = expenses.groupBy { it.category }
                    )
                }
            }.catch { error ->
                _uiState.value = HomeUiState.Error(error.message ?: "Unknown error occurred")
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun updateMonthlyBudget(budget: Double) {
        viewModelScope.launch {
            try {
                userRepository.updateMonthlyBudget(budget)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Failed to update budget")
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                // First delete from Firestore/local storage
                // Then sign out
                authRepository.signOut()
                _uiState.value = HomeUiState.NotAuthenticated
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Failed to delete account")
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    object NotAuthenticated : HomeUiState()
    data class Success(
        val user: User,
        val expenses: List<Expense>,
        val expensesByCategory: Map<ExpenseCategory, List<Expense>>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
} 