package com.example.travelocity.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.travelocity.Domains.Tour;
import com.example.travelocity.R;
import com.example.travelocity.databinding.ActivityUpdateDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateDetails extends TourBaseActivity {
    private ActivityUpdateDetailsBinding binding;
    private Tour tourObject;
    private DatabaseReference toursRef;
    private ProgressDialog progressDialog;
    private Uri imageUri;
    private String tourId;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    onImageSelected(result.getData());
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdateDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get tour ID from intent
        tourId = getIntent().getStringExtra("tour_id");
        Log.d("UpdateDetails", "Tour with ID " + tourId + " not found.");
        if (tourId == null) {
            Toast.makeText(this, "Invalid Tour ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        toursRef = FirebaseDatabase.getInstance().getReference("tours");
        fetchTourDetails(tourId);

        binding.selectImageBtn.setOnClickListener(v -> selectTourImage());
        binding.updateTourBtn.setOnClickListener(v -> updateTour());
    }

    private void fetchTourDetails(String tourId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Tour Details...");
        progressDialog.show();

        toursRef.child(tourId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tourObject = dataSnapshot.getValue(Tour.class);
                    setInitialValues();
                } else {
                    Toast.makeText(UpdateDetails.this, "Tour not found", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(UpdateDetails.this, "Failed to fetch tour details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setInitialValues() {
        if (tourObject != null) {
            Glide.with(this).load(tourObject.getPic()).into(binding.firebaseTourImage);
            binding.tourTitle.setText(tourObject.getTitle());
            binding.tourLocation.setText(tourObject.getLocation());
            binding.tourDescription.setText(tourObject.getDescription());
            binding.tourBed.setText(String.valueOf(tourObject.getBed()));
            binding.tourPrice.setText(String.valueOf(tourObject.getPrice()));
            binding.hasWifi.setChecked(tourObject.isWifi());
            binding.hasGuide.setChecked(tourObject.isGuide());
            binding.autoCompleteText.setText(tourObject.getCategory(), false);
        }
    }

    private void updateTour() {
        String title = binding.tourTitle.getText().toString().trim();
        String location = binding.tourLocation.getText().toString().trim();
        String description = binding.tourDescription.getText().toString().trim();
        String bedStr = binding.tourBed.getText().toString().trim();
        String priceStr = binding.tourPrice.getText().toString().trim();

        if (!validateInputs(title, location, description, bedStr, priceStr)) return;

        int bed = Integer.parseInt(bedStr);
        double price = Double.parseDouble(priceStr);
        boolean guide = binding.hasGuide.isChecked();
        boolean wifi = binding.hasWifi.isChecked();
        String categoryTitle = binding.autoCompleteText.getText().toString().trim();
        if (imageUri != null) {
            uploadImageAndUpdateTour(createTourObject(title, location, description, bed, price, guide, wifi, categoryTitle, ""), tourId);
        } else {
            updateTourInDatabase(createTourObject(title, location, description, bed, price, guide, wifi, categoryTitle, tourObject.getPic()), tourId);
        }
    }

    private void updateTourInDatabase(Tour tour, String tourId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Tour...");
        progressDialog.show();

        toursRef.child(tourId).setValue(tour)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Tour updated successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Tours_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to update tour: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImageAndUpdateTour(Tour tour, String tourId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();
        String fileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA).format(new Date()) + ".jpg";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            tour.setPic(uri.toString());
                            updateTourInDatabase(tour, tourId);
                            progressDialog.dismiss();
                        }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void onImageSelected(Intent data) {
        imageUri = data.getData();
        binding.firebaseTourImage.setImageURI(imageUri);
    }

    @Override
    protected void selectTourImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}
