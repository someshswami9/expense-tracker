package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.expensetracker.ui.navigation.ExpenseTrackerNavigation
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.util.NotificationHelper
import com.example.expensetracker.util.RequestNotificationPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private var hasNotificationPermission by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check initial permission state
        hasNotificationPermission = notificationHelper.checkNotificationPermission()

        setContent {
            ExpenseTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RequestNotificationPermission { isGranted ->
                        hasNotificationPermission = isGranted
                    }
                    
                    ExpenseTrackerNavigation()
                }
            }
        }
    }
} 