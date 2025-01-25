package com.example.expensetracker.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val messaging: FirebaseMessaging
) {
    suspend fun updateFcmToken(userId: String) {
        try {
            val token = messaging.token.await()
            firestore.collection("users")
                .document(userId)
                .update("fcmToken", token)
                .await()
        } catch (e: Exception) {
            // Handle token update failure
        }
    }
} 