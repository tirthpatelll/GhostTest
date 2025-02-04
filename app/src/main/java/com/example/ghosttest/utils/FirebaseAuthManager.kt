package com.example.ghosttest.utils

import android.util.Log
import com.example.ghosttest.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.FieldValue

class FirebaseAuthManager {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun registerUser(email: String, password: String, username: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get the newly created user's UID
                    val userId = auth.currentUser?.uid ?: run {
                        onComplete(false, "User ID is null")
                        return@addOnCompleteListener
                    }

                    // Create a User object
                    val user = User(
                        userId = userId,
                        username = username,
                        email = email,
                        totalXp = 0,
                        ingameLevel = 1,
                        badges = emptyList(),
                        unlockedCharacters = emptyList(),
                        currentLocation = GeoPoint(0.0, 0.0), // Default location
                       ///dateJoined = FieldValue.serverTimestamp()
                    )

                    // Add/update the user document in Firestore
                    db.collection("users")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d("Registration", "User added to Firestore")
                            onComplete(true, null)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Registration", "Firestore error: ${e.message}")
                            onComplete(false, e.message)
                        }
                } else {
                    Log.e("Registration", "Auth error: ${task.exception?.message}")
                    onComplete(false, task.exception?.message)
                }
            }
    }
}