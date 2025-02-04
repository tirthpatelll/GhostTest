package com.example.ghosttest.utils

import android.util.Log
import com.example.ghosttest.models.UserProgress
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class FirebaseManager {
    private val db = FirebaseFirestore.getInstance()

    // Update user's location in Firestore
    fun updateUserLocation(userId: String, latitude: Double, longitude: Double) {
        db.collection("users").document(userId)
            .update("currentLocation", GeoPoint(latitude, longitude))
            .addOnSuccessListener {
                Log.d("Firebase", "Location updated")
            }
    }

    // Complete a quiz and update XP
    fun completeQuiz(userId: String, locationId: String, quizId: String) {
        val userRef = db.collection("users").document(userId)
        val progressRef = db.collection("users/$userId/userProgress")

        db.collection("hauntedLocations/$locationId/quizzes").document(quizId)
            .get()
            .addOnSuccessListener { quizDoc ->
                val xpReward = quizDoc.getLong("xpReward")?.toInt() ?: 0

                db.runTransaction { transaction ->
                    val userDoc = transaction.get(userRef)
                    val currentXp = userDoc.getLong("totalXp")?.toInt() ?: 0
                    val newXp = currentXp + xpReward

                    transaction.update(userRef, "totalXp", newXp)
                    transaction.set(
                        progressRef.document(),
                        UserProgress(
                            locationId = locationId,
                            quizCompleted = true,
                            xpEarned = xpReward
                        )
                    )
                }
            }
    }
}