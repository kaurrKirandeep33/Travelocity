package com.example.travelocity.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.travelocity.Domains.Tour;
import com.example.travelocity.databinding.ActivityNewToursBinding;
import com.google.firebase.database.DatabaseReference;

public class NewTours extends TourBaseActivity {
    private ActivityNewToursBinding binding;
    private DatabaseReference toursRef;
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            onImageSelected(result.getData());
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewToursBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toursRef = firebaseDatabase.getReference("tours");
        initializeCategoryDropdown(binding.autoCompleteText);
        binding.selectImageBtn.setOnClickListener(v -> selectTourImage());
        binding.selectCreateTourBtn.setOnClickListener(v -> createNewTour());
    }

    private void createNewTour() {
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
        String categoryTitle = autoCompleteTextView.getText().toString().trim();
        Tour tour = createTourObject(title, location, description, bed, price, guide, wifi, categoryTitle, "");
        if (imageUri != null) {
            uploadImageAndSaveTour(tour, toursRef, this::clearForm);
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void onImageSelected(Intent data) {
        imageUri = data.getData();
        binding.firebaseTourImage.setImageURI(imageUri);
    }

    protected void selectTourImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
    private void clearForm() {
        binding.tourTitle.setText("");
        binding.tourLocation.setText("");
        binding.tourDescription.setText("");
        binding.tourBed.setText("");
        binding.tourPrice.setText("");
        binding.hasGuide.setChecked(false);
        binding.hasWifi.setChecked(false);
        autoCompleteTextView.setText("");
        binding.firebaseTourImage.setImageURI(null);
        imageUri = null;
    }
}
