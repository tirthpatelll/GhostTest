package com.example.ghosttest.models

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val totalXp: Int = 0,
    val ingameLevel: Int = 1,
    val badges: List<String> = emptyList(),
    val unlockedCharacters: List<String> = emptyList(),
    val currentLocation: GeoPoint = GeoPoint(0.0, 0.0),
    @ServerTimestamp // Auto-populate on Firestore server
    val dateJoined: Date? = null // Firestore will set this automatically
) {
    fun toFirestoreMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "username" to username,
            "email" to email,
            "totalXp" to totalXp,
            "ingameLevel" to ingameLevel,
            "badges" to this.badges,
            "unlockedCharacters" to unlockedCharacters,
            "currentLocation" to currentLocation,
            "dateJoined" to FieldValue.serverTimestamp()
        )
    }
}