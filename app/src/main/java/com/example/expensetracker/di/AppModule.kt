package com.example.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.example.expensetracker.data.local.ExpenseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideExpenseDatabase(
        @ApplicationContext context: Context
    ): ExpenseDatabase = Room.databaseBuilder(
        context,
        ExpenseDatabase::class.java,
        "expense_db"
    ).build()

    @Provides
    @Singleton
    fun provideUserDao(db: ExpenseDatabase) = db.userDao()

    @Provides
    @Singleton
    fun provideExpenseDao(db: ExpenseDatabase) = db.expenseDao()
} 