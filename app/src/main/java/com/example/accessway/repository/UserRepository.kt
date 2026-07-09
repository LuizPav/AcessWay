package com.example.accessway.repository

import com.example.accessway.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveUser(user: UserProfile): Result<Unit> {
        return try {
            // Garante que não salvaremos sem um UID
            if (user.uid.isEmpty()) throw Exception("UID do usuário vazio")

            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(uid: String): Result<UserProfile> {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            // O .copy(uid = document.id) garante que o objeto carregado tenha o ID correto
            val user = document.toObject(UserProfile::class.java)?.copy(uid = document.id)
                ?: throw Exception("Usuário não encontrado")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}