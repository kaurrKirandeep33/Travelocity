package com.example.travelocity.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelocity.Adapter.CategoryAdapter;
import com.example.travelocity.Domains.Category;
import com.example.travelocity.R;
import com.example.travelocity.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        setVariables();
    }


    private void setVariables() {
        binding.getStarted.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Login_activity.class);
            startActivity(intent);
            finish();
        });
    }
}