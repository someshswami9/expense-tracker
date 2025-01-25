package com.example.expensetracker.ui.screens.addexpense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.data.model.ExpenseCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navController: NavController,
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ExpenseCategory?>(null) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AddExpenseUiState.Success) {
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { showCategoryDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedCategory?.name ?: "Select Category")
            }

            if (uiState is AddExpenseUiState.Error) {
                Text(
                    text = (uiState as AddExpenseUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (amountValue != null && selectedCategory != null) {
                        viewModel.addExpense(
                            amount = amountValue,
                            category = selectedCategory!!,
                            description = description
                        )
                    }
                },
                enabled = amount.isNotEmpty() && selectedCategory != null && 
                         uiState !is AddExpenseUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Expense")
            }

            if (uiState is AddExpenseUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally)
                )
            }
        }

        if (showCategoryDialog) {
            AlertDialog(
                onDismissRequest = { showCategoryDialog = false },
                title = { Text("Select Category") },
                text = {
                    Column {
                        ExpenseCategory.values().forEach { category ->
                            TextButton(
                                onClick = {
                                    selectedCategory = category
                                    showCategoryDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(category.name)
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }
} 