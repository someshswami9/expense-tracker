package com.example.expensetracker.data.model

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val monthlyBudget: Double = 0.0
) 