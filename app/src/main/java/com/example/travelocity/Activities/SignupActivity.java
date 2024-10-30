package com.example.travelocity.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelocity.R;
import com.example.travelocity.databinding.ActivitySignupBinding;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariabe();
    }

    private void setVariabe() {
        binding.btnSignUp.setOnClickListener(v -> {
            String email = binding.editEmail.getText().toString();
            String password = binding.editPassword.getText().toString();

            if(password.length() < 6){
                Toast.makeText(this, "Your password is too short", Toast.LENGTH_SHORT).show();
                return;
            }
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Log.i(TAG, "createUserWithEmailAndPassword: Success", task.getException());
                    startActivity(new Intent(SignupActivity.this, Login_activity.class));
                } else{
                    Log.i(TAG, "createUserWithEmailAndPassword: Failed", task.getException());
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}