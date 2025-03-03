package com.example.ghosttest.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ghosttest.MainActivity;
import com.example.ghosttest.R;
import com.example.ghosttest.utils.FireStoreManager;
import com.example.ghosttest.utils.FirebaseAuthManager;

public class UserProfileActivity extends AppCompatActivity {

    private FireStoreManager firestoreManager;
    private FirebaseAuthManager authManager;

    private TextView tvUsername, tvLevel, tvXp;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        authManager = new FirebaseAuthManager(this);
        firestoreManager = new FireStoreManager();
        tvUsername = findViewById(R.id.username_value);
        tvLevel = findViewById(R.id.in_game_level_value);
        tvXp = findViewById(R.id.total_xp_value);
        btnBack = findViewById(R.id.btn_back);
        loadUserData();
        btnBack.setOnClickListener(v ->
                new Intent(UserProfileActivity.this, MainActivity.class)
                );
    }

    private void loadUserData() {
        // Fetch user data from Firestore
        String userId = authManager.getUserID();
        firestoreManager.getUserData(userId,
                documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        try {
                            long level = documentSnapshot.getLong("ingameLevel");
                            long xp = documentSnapshot.getLong("totalXp");
                            // Update UI
                            tvUsername.setText(username);
                            tvLevel.setText(String.valueOf(level));
                            tvXp.setText(xp + "XP");
                            Log.d("Firestore", "User data fetched successfully");
                        } catch (NullPointerException e) {
                            Log.e("Firestore", "Error fetching user data", e);
                        }
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                },
                e -> Log.e("Firestore", "Error fetching user data", e)
        );
    }

}
