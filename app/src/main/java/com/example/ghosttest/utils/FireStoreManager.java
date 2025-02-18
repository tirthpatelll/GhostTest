package com.example.ghosttest.utils;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class FireStoreManager {
    private FirebaseFirestore db;
    private final String USER_COLLECTION = "users";
    public FireStoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void getUserData(String userId, OnSuccessListener<DocumentSnapshot> onSuccess, OnFailureListener onFailure) {
        Task<DocumentSnapshot> userRef = db.collection(USER_COLLECTION).document(userId)
                .get()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);

    }
}

