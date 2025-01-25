package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.dao.UserDao
import com.example.expensetracker.data.local.entity.UserEntity
import com.example.expensetracker.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao
) {
    private val userId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

    fun getCurrentUser(): Flow<User?> {
        return userDao.getUserFlow(userId).map { it?.toUser() }
    }

    suspend fun updateMonthlyBudget(budget: Double) {
        val user = userDao.getUser(userId)?.toUser()?.copy(monthlyBudget = budget)
            ?: throw IllegalStateException("User not found")

        // Update Firestore
        try {
            firestore.collection("users")
                .document(userId)
                .set(user)
                .await()
        } catch (e: Exception) {
            // Handle Firestore error
        }

        // Update local database
        userDao.insertUser(user.toEntity())
    }

    private fun UserEntity.toUser() = User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        photoUrl = photoUrl,
        monthlyBudget = monthlyBudget
    )

    private fun User.toEntity() = UserEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        photoUrl = photoUrl,
        monthlyBudget = monthlyBudget
    )
} 