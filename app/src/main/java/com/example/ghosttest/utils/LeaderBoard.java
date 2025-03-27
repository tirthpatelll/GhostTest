package com.example.ghosttest.utils;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.ghosttest.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class LeaderBoard {
    private static final String TAG = "LeaderBoard";
    private final FirebaseFirestore db;

    public LeaderBoard() {
        db = FirebaseFirestore.getInstance();
    }

    // Fetch top users sorted by XP
    public void getTopUsers(int limit, OnLeaderboardFetchedListener listener) {
        db.collection("users")
                .orderBy("totalXp", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<User> topUsers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert Firestore document to your User class
                                User user = document.toObject(User.class);
                                topUsers.add(user);
                            }
                            listener.onLeaderboardFetched(topUsers);
                        } else {
                            Log.e(TAG, "Error fetching leaderboard", task.getException());
                            listener.onLeaderboardFetchFailed(task.getException());
                        }
                    }
                });
    }

    // Listener interface for leaderboard callbacks
    public interface OnLeaderboardFetchedListener {
        void onLeaderboardFetched(List<User> topUsers);
        void onLeaderboardFetchFailed(Exception e);
    }
}
