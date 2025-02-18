package com.example.ghosttest.utils

import android.content.Context
import android.util.Log
import com.example.ghosttest.models.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class FirebaseAuthManager(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    fun registerUser(email: String, password: String, username: String, onComplete: (Boolean, String?) -> Unit) {
        // 1. Check username availability
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener { usernameCheckTask ->
                if (usernameCheckTask.isSuccessful) {
                    if (!usernameCheckTask.result!!.isEmpty) {
                        onComplete(false, "Username already exists")
                        return@addOnCompleteListener
                    }

                    // 2. Get device location
                    getCurrentLocation { geoPoint ->
                        // 3. Create Auth user
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    val userId = auth.currentUser?.uid ?: run {
                                        onComplete(false, "User ID is null")
                                        return@addOnCompleteListener
                                    }

                                    // 4. Create User object
                                    val user = User(
                                        userId = userId,
                                        username = username,
                                        email = email,
                                        currentLocation = geoPoint ?: GeoPoint(0.0, 0.0)
                                    )

                                    // 5. Save to Firestore
                                    db.collection("users")
                                        .document(userId)
                                        .set(user.toFirestoreMap())
                                        .addOnSuccessListener {
                                            onComplete(true, null)
                                        }
                                        .addOnFailureListener { e ->
                                            // Clean up auth user if Firestore fails
                                            auth.currentUser?.delete()
                                            onComplete(false, "Firestore error: ${e.message}")
                                        }
                                } else {
                                    onComplete(false, "Auth failed: ${authTask.exception?.message}")
                                }
                            }
                    }
                } else {
                    onComplete(false, "Username check failed: ${usernameCheckTask.exception?.message}")
                }
            }
    }

    private fun getCurrentLocation(callback: (GeoPoint?) -> Unit) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        callback(GeoPoint(location.latitude, location.longitude))
                    } else {
                        callback(null)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Location", "Error getting location: ${e.message}")
                    callback(null)
                }
        } catch (e: SecurityException) {
            Log.e("Location", "Location permission denied")
            callback(null)
        }
    }

    public fun getUserID(): String {
        return auth.currentUser?.uid ?: ""
    }

}