package com.example.ghosttest.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ghosttest.MainActivity;
import com.example.ghosttest.R;
import com.example.ghosttest.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get references to UI elements
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        Button registerButton = binding.register;
        registerButton.setOnClickListener(v -> {
            // Navigate to RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });



        // Add text watchers to validate input dynamically
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isDataValid = !usernameEditText.getText().toString().isEmpty()
                        && !passwordEditText.getText().toString().isEmpty();
                loginButton.setEnabled(isDataValid);
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        // Handle login button click
        loginButton.setOnClickListener(v -> {
            String email = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loadingProgressBar.setVisibility(View.VISIBLE);

            // Firebase Authentication Login
            mAuth.signInWithEmailAndPassword("tirthbsq@gmail.com", "Tirth0812")
                    .addOnCompleteListener(task -> {
                        loadingProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.d("LoginActivity", "Login successful");
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            // Navigate to the next activity or main screen
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
