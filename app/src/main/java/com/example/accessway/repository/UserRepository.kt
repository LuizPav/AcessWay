package com.example.accessway.repository

import com.example.accessway.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

//Responsabilidade: salvar e buscar informações do usuário.
class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveUser(user: UserProfile): Result<Unit> {

        return try {

            firestore
                .collection("users")
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

            val document = firestore
                .collection("users")
                .document(uid)
                .get()
                .await()

            val user = document.toObject(UserProfile::class.java)
                ?: throw Exception("Usuário não encontrado")

            Result.success(user)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}