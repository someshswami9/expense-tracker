package com.example.expensetracker.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.ui.navigation.Screen
import java.text.NumberFormat
import java.util.*
import com.example.expensetracker.ui.components.charts.ExpensePieChart
import com.example.expensetracker.ui.components.charts.ExpenseLineChart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import com.example.expensetracker.ui.components.drawer.AppDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is HomeUiState.NotAuthenticated) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (uiState is HomeUiState.Success) {
                AppDrawer(
                    user = (uiState as HomeUiState.Success).user,
                    onMonthlyBudgetClick = { showBudgetDialog = true },
                    onDeleteAccountClick = { showDeleteAccountDialog = true },
                    onLogoutClick = { viewModel.signOut() },
                    onPrivacyPolicyClick = { /* TODO: Implement privacy policy */ },
                    onNotificationSettingsClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screen.NotificationSettings.route)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        when (val state = uiState) {
                            is HomeUiState.Success -> {
                                Text("Hi, ${state.user.firstName}")
                            }
                            else -> Text("Expense Tracker")
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { 
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sign Out") },
                                onClick = {
                                    viewModel.signOut()
                                    showMenu = false
                                }
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddExpense.route) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }
            }
        ) { paddingValues ->
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is HomeUiState.Success -> {
                    HomeContent(
                        state = state,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
                is HomeUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {}
            }
        }

        if (showBudgetDialog) {
            MonthlyBudgetDialog(
                currentBudget = (uiState as? HomeUiState.Success)?.user?.monthlyBudget ?: 0.0,
                onDismiss = { showBudgetDialog = false },
                onConfirm = { budget ->
                    viewModel.updateMonthlyBudget(budget)
                    showBudgetDialog = false
                }
            )
        }
        
        if (showDeleteAccountDialog) {
            DeleteAccountDialog(
                onDismiss = { showDeleteAccountDialog = false },
                onConfirm = {
                    viewModel.deleteAccount()
                    showDeleteAccountDialog = false
                }
            )
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState.Success,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ExpensePieChart(
            expensesByCategory = state.expensesByCategory,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ExpenseLineChart(
            expenses = state.expenses,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Display total expenses by category
        state.expensesByCategory.forEach { (category, expenses) ->
            val total = expenses.sumOf { it.amount }
            val count = expenses.size
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                ListItem(
                    headlineContent = { Text(category.name) },
                    supportingContent = { 
                        Text("${currencyFormat.format(total)} ($count transactions)")
                    }
                )
            }
        }
    }
} 