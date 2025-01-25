package com.example.expensetracker.ui.components.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.data.model.ExpenseCategory
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.pie.pieChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpensePieChart(
    expensesByCategory: Map<ExpenseCategory, List<Expense>>,
    modifier: Modifier = Modifier
) {
    val categoryColors = remember {
        ExpenseCategory.values().associateWith {
            Color.hsl(
                hue = Random().nextFloat() * 360f,
                saturation = 0.7f,
                lightness = 0.5f
            )
        }
    }

    val pieChartData = expensesByCategory.map { (category, expenses) ->
        val total = expenses.sumOf { it.amount }
        category to total
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Expenses by Category",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Chart(
                chart = pieChart(),
                model = entryModelOf(pieChartData.map { it.second.toFloat() }),
                modifier = Modifier.weight(1f)
            )
            
            // Legend
            Column {
                pieChartData.forEach { (category, total) ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(12.dp),
                            color = categoryColors[category] ?: Color.Gray
                        ) {}
                        Text(
                            text = "${category.name}: ${
                                NumberFormat.getCurrencyInstance().format(total)
                            }"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseLineChart(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("MM/dd", Locale.getDefault()) }
    
    // Group expenses by date and calculate daily totals
    val dailyTotals = expenses
        .groupBy { dateFormat.format(it.date) }
        .mapValues { it.value.sumOf { expense -> expense.amount } }
        .toList()
        .sortedBy { it.first }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Daily Expenses Trend",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Chart(
                chart = lineChart(),
                model = entryModelOf(dailyTotals.map { it.second.toFloat() }),
                startAxis = startAxis(),
                bottomAxis = bottomAxis(),
                modifier = Modifier.weight(1f)
            )
        }
    }
} 