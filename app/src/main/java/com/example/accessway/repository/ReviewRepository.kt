package com.example.accessway.repository

import com.example.accessway.model.Review
import com.google.firebase.firestore.FirebaseFirestore

class ReviewRepository {

    private val db = FirebaseFirestore.getInstance()

    private val reviews = db.collection("reviews")

    fun saveReview(
        review: Review,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        val id = reviews.document().id

        reviews.document(id)

            .set(review.copy(id = id))

            .addOnSuccessListener {

                onSuccess()

            }

            .addOnFailureListener {

                onError(it)

            }

    }

}