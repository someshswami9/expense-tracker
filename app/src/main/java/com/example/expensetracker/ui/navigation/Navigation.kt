package com.example.expensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.ui.screens.login.LoginScreen
import com.example.expensetracker.ui.screens.home.HomeScreen
import com.example.expensetracker.ui.screens.addexpense.AddExpenseScreen
import com.example.expensetracker.ui.screens.notificationsettings.NotificationSettingsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object AddExpense : Screen("add_expense")
    object Profile : Screen("profile")
    object NotificationSettings : Screen("notification_settings")
}

@Composable
fun ExpenseTrackerNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.AddExpense.route) {
            AddExpenseScreen(navController = navController)
        }
        
        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(navController = navController)
        }
        
        // Additional routes will be added later
    }
} 