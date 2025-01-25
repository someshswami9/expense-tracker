package com.example.expensetracker.ui.screens.addexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.data.model.ExpenseCategory
import com.example.expensetracker.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddExpenseUiState>(AddExpenseUiState.Initial)
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    fun addExpense(amount: Double, category: ExpenseCategory, description: String) {
        viewModelScope.launch {
            _uiState.value = AddExpenseUiState.Loading
            try {
                val expense = Expense(
                    id = UUID.randomUUID().toString(),
                    amount = amount,
                    category = category,
                    description = description,
                    date = Date()
                )
                expenseRepository.addExpense(expense)
                _uiState.value = AddExpenseUiState.Success
            } catch (e: Exception) {
                _uiState.value = AddExpenseUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

sealed class AddExpenseUiState {
    object Initial : AddExpenseUiState()
    object Loading : AddExpenseUiState()
    object Success : AddExpenseUiState()
    data class Error(val message: String) : AddExpenseUiState()
} 