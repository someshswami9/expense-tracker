package com.example.expensetracker.data.repository

import com.example.expensetracker.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: Flow<User?> = _currentUser

    init {
        auth.currentUser?.let { firebaseUser ->
            _currentUser.value = User(
                id = firebaseUser.uid,
                firstName = firebaseUser.displayName?.split(" ")?.firstOrNull() ?: "",
                lastName = firebaseUser.displayName?.split(" ")?.lastOrNull() ?: "",
                email = firebaseUser.email ?: "",
                photoUrl = firebaseUser.photoUrl?.toString() ?: ""
            )
        }
    }

    suspend fun signInWithGoogle(idToken: String): Result<User> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()
        val firebaseUser = authResult.user!!
        
        val user = User(
            id = firebaseUser.uid,
            firstName = firebaseUser.displayName?.split(" ")?.firstOrNull() ?: "",
            lastName = firebaseUser.displayName?.split(" ")?.lastOrNull() ?: "",
            email = firebaseUser.email ?: "",
            photoUrl = firebaseUser.photoUrl?.toString() ?: ""
        )
        _currentUser.value = user
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = null
    }
} 