package com.example.travelocity.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelocity.R;
import com.example.travelocity.databinding.ActivityLoginBinding;

public class Login_activity extends BaseActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariables();
        binding.singUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(Login_activity.this, SignupActivity.class));
            finish();
        });
        binding.btnExit.setOnClickListener(v -> {
            startActivity(new Intent(Login_activity.this, MainActivity.class));
        });
    }

    private void setVariables() {
        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.loginEmailText.getText().toString();
            String password = binding.loginPassText.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(Login_activity.this, Tours_Activity.class));
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}