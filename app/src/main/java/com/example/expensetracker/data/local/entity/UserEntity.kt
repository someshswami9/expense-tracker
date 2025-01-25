package com.example.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val photoUrl: String,
    val monthlyBudget: Double
) 