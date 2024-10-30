package com.example.travelocity.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.travelocity.Domains.Tour;
import com.example.travelocity.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class TourBaseActivity extends BaseActivity {

    protected AutoCompleteTextView autoCompleteTextView;
    protected ArrayAdapter<String> adapterItems;
    protected Uri imageUri;
    protected ProgressDialog progressDialog;
    protected StorageReference storageReference;
    protected String[] category = {"Adventure", "Cultural", "Wildlife", "Historical", "Beach"};

    protected void initializeCategoryDropdown(AutoCompleteTextView autoCompleteTextView) {
        this.autoCompleteTextView = autoCompleteTextView;
        adapterItems = new ArrayAdapter<>(this, R.layout.list_category, category);
        autoCompleteTextView.setAdapter(adapterItems);
    }

    protected Tour createTourObject(String title, String location, String description, int bed, double price, boolean guide, boolean wifi, String categoryTitle, String imageUrl) {
        return new Tour(null, title, location, description, bed, guide, 0, imageUrl, wifi, price, categoryTitle);
    }

    protected boolean validateInputs(String title, String location, String description, String bedStr, String priceStr) {
        if (title.isEmpty() || location.isEmpty() || description.isEmpty() || bedStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    protected void uploadImageAndSaveTour(Tour tour, DatabaseReference toursRef, Runnable postUploadAction) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();

        String fileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA).format(new Date()) + ".jpg";
        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            tour.setPic(uri.toString());
                            saveTourToDatabase(tour, toursRef, postUploadAction);
                            progressDialog.dismiss();
                        }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                });
    }

    protected void saveTourToDatabase(Tour tour, DatabaseReference toursRef, Runnable postSaveAction) {
        String tourId = tour.getId() != null ? tour.getId() : toursRef.push().getKey();
        if (tourId != null) {
            toursRef.child(tourId).setValue(tour)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Tour created successfully!", Toast.LENGTH_SHORT).show();
                        postSaveAction.run();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to save tour: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Failed to generate unique ID for tour.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void selectTourImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
    }

}
