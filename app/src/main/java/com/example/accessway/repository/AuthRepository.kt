package com.example.accessway.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

//Responsabilidade: Login, cadastro e logout.
class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    suspend fun register(
        email: String,
        password: String
    ): Result<String> {
        return try {

            val result = auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            Result.success(result.user!!.uid)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<String> {
        return try {

            val result = auth
                .signInWithEmailAndPassword(email, password)
                .await()

            Result.success(result.user!!.uid)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }
}