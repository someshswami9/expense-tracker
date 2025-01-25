package com.example.expensetracker.ui.components.drawer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.expensetracker.data.model.User
import java.text.NumberFormat
import java.util.*
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    user: User,
    onMonthlyBudgetClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onNotificationSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier = modifier) {
        DrawerHeader(user)
        DrawerBody(
            monthlyBudget = user.monthlyBudget,
            onMonthlyBudgetClick = onMonthlyBudgetClick,
            onDeleteAccountClick = onDeleteAccountClick,
            onLogoutClick = onLogoutClick,
            onPrivacyPolicyClick = onPrivacyPolicyClick,
            onNotificationSettingsClick = onNotificationSettingsClick
        )
    }
}

@Composable
private fun DrawerHeader(
    user: User,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.photoUrl,
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "${user.firstName} ${user.lastName}",
            style = MaterialTheme.typography.titleMedium
        )
        
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun DrawerBody(
    monthlyBudget: Double,
    onMonthlyBudgetClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onNotificationSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ListItem(
            headlineContent = { Text("Monthly Budget") },
            supportingContent = { 
                Text(NumberFormat.getCurrencyInstance().format(monthlyBudget))
            },
            leadingContent = {
                Icon(Icons.Default.AccountBalance, contentDescription = null)
            },
            modifier = Modifier.clickable(onClick = onMonthlyBudgetClick)
        )
        
        Divider()

        ListItem(
            headlineContent = { Text("Delete Account") },
            leadingContent = {
                Icon(
                    Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            modifier = Modifier.clickable(onClick = onDeleteAccountClick)
        )

        ListItem(
            headlineContent = { Text("Logout") },
            leadingContent = {
                Icon(Icons.Default.Logout, contentDescription = null)
            },
            modifier = Modifier.clickable(onClick = onLogoutClick)
        )

        ListItem(
            headlineContent = { Text("Privacy Policy") },
            leadingContent = {
                Icon(Icons.Default.Policy, contentDescription = null)
            },
            modifier = Modifier.clickable(onClick = onPrivacyPolicyClick)
        )

        ListItem(
            headlineContent = { Text("Notification Settings") },
            leadingContent = {
                Icon(Icons.Default.Notifications, contentDescription = null)
            },
            modifier = Modifier.clickable(onClick = onNotificationSettingsClick)
        )

        Divider()
    }
} 