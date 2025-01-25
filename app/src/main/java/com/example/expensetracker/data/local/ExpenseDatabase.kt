package com.example.expensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensetracker.data.local.dao.ExpenseDao
import com.example.expensetracker.data.local.dao.UserDao
import com.example.expensetracker.data.local.entity.ExpenseEntity
import com.example.expensetracker.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
} 