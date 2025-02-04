package com.example.ghosttest.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.FieldValue

class FirebaseAuthManager {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun registerUser(email: String, password: String, username: String, onComplete: (Boolean, String?) -> Unit) {
        // First check if username already exists
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener { usernameCheckTask ->
                if (usernameCheckTask.isSuccessful) {
                    // Username is already taken
                    if (!usernameCheckTask.result?.isEmpty!!) {
                        onComplete(false, "Username already exists. Please choose another one.")
                        return@addOnCompleteListener
                    }

                    // If username is available, proceed with registration
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                val userId = auth.currentUser?.uid ?: run {
                                    onComplete(false, "User ID is null")
                                    return@addOnCompleteListener
                                }

                                // Create user document
                                val userMap = hashMapOf(
                                    "userId" to userId,
                                    "username" to username,
                                    "email" to email,
                                    "totalXp" to 0,
                                    "ingameLevel" to 1,
                                    "badges" to emptyList<String>(),
                                    "unlockedCharacters" to emptyList<String>(),
                                    "currentLocation" to GeoPoint(0.0, 0.0),
                                    "dateJoined" to FieldValue.serverTimestamp()
                                )

                                db.collection("users")
                                    .document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        onComplete(true, null)
                                    }
                                    .addOnFailureListener { e ->
                                        // Delete auth user if Firestore write fails
                                        auth.currentUser?.delete()
                                        onComplete(false, "Failed to create user profile: ${e.message}")
                                    }
                            } else {
                                onComplete(false, "Authentication failed: ${authTask.exception?.message}")
                            }
                        }
                } else {
                    onComplete(false, "Error checking username: ${usernameCheckTask.exception?.message}")
                }
            }
    }
}