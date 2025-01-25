package com.example.expensetracker.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    navController: NavController,
    viewModel: NotificationSettingsViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferences.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Settings") },
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
            NotificationSettingItem(
                title = "Expense Warnings",
                description = "Get notified when expenses exceed category limits",
                checked = preferences.expenseWarnings,
                onCheckedChange = viewModel::updateExpenseWarnings
            )

            NotificationSettingItem(
                title = "Budget Warnings",
                description = "Get notified when approaching monthly budget limit",
                checked = preferences.budgetWarnings,
                onCheckedChange = viewModel::updateBudgetWarnings
            )

            NotificationSettingItem(
                title = "Daily Recap",
                description = "Receive daily summary of expenses",
                checked = preferences.dailyRecap,
                onCheckedChange = viewModel::updateDailyRecap
            )

            NotificationSettingItem(
                title = "Monthly Recap",
                description = "Receive monthly expense summary",
                checked = preferences.monthlyRecap,
                onCheckedChange = viewModel::updateMonthlyRecap
            )
        }
    }
}

@Composable
private fun NotificationSettingItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
} 