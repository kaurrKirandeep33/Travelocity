package com.example.travelocity.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelocity.Adapter.CategoryAdapter;
import com.example.travelocity.Adapter.DestinationHolder;
import com.example.travelocity.Domains.Category;
import com.example.travelocity.Domains.Tour;
import com.example.travelocity.R;
import com.example.travelocity.databinding.ActivityToursBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tours_Activity extends BaseActivity {
    ActivityToursBinding binding;
    ArrayList<Tour> tourModel = new ArrayList<>();
    DestinationHolder destinationHolder;
    RecyclerView recCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToursBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
        initTours();
        initRecyclerViewCategory();
    }

    private void initRecyclerViewCategory() {
        ArrayList<Category> categoryModels = new ArrayList<>();
        categoryModels.add(new Category("Adventure", "cat1"));
        categoryModels.add(new Category("City", "cat2"));
        categoryModels.add(new Category("Beaches", "cat3"));
        categoryModels.add(new Category("Mountain", "cat4"));
        categoryModels.add(new Category("Ocean", "cat5"));
         recCategory = findViewById(R.id.viewCategoryView);
         recCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
         CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModels);
         recCategory.setAdapter(categoryAdapter);
    }

    private void initTours() {
        binding.viewDestinaton.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        destinationHolder = new DestinationHolder(tourModel);
        binding.viewDestinaton.setAdapter(destinationHolder);
        DatabaseReference databaseReference = firebaseDatabase.getReference("tours");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tourModel.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Tour tour = dataSnapshot.getValue(Tour.class);
                        if (tour != null) {
                            tour.setId(dataSnapshot.getKey());
                            tourModel.add(tour);
                        }
                    }
                    destinationHolder.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Tours_Activity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVariable() {
        binding.addDestination.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewTours.class);
            startActivity(intent);
        });
    }
}
