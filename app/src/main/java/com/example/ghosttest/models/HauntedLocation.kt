package com.example.ghosttest.models

import com.google.firebase.firestore.GeoPoint

data class HauntedLocation(
    val locationId: String = "",
    val name: String = "",
    val description: String = "",
    val coordinates: GeoPoint = GeoPoint(0.0, 0.0),
    val iconUrl: String = "",
    val unlockDistanceMeters: Int = 50
)