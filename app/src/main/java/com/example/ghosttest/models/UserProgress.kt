package com.example.ghosttest.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class UserProgress(
    val locationId: String = "",
    val quizCompleted: Boolean = false,
    val xpEarned: Int = 0,
    @ServerTimestamp val timestamp: Date? = null
)