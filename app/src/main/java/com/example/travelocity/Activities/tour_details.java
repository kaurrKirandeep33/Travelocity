package com.example.travelocity.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.travelocity.Domains.Tour;
import com.example.travelocity.R;
import com.example.travelocity.databinding.ActivityTourDetailsBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class tour_details extends BaseActivity {
    ActivityTourDetailsBinding binding;
    private Tour object;
    private DatabaseReference toursRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTourDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();

        binding.backBtn.setOnClickListener(v -> finish());
        binding.updateBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateDetails.class);
            intent.putExtra("tour_id", object.getId());
            startActivity(intent);
        });

        binding.deleteBtn.setOnClickListener(v -> {
            showDeleteConfirmationDialog();
        });
        toursRef = FirebaseDatabase.getInstance().getReference("tours");
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Tour")
                .setMessage("Are you sure you want to delete this tour?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTour();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteTour() {
        if (object != null && object.getId() != null) {
            toursRef.child(object.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Tour deleted successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to delete tour: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Tour ID is null. Cannot delete.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setVariable() {
        Glide.with(this).load(object.getPic())
                .into(binding.tourPic);
        binding.tourTitle.setText(object.getTitle());
        binding.locaionTxt.setText(object.getLocation());
        binding.scoreTxt.setText(String.valueOf(object.getScore()));
        binding.bedTxt.setText(String.valueOf(object.getBed()));

        binding.wifiTxt.setText(object.isWifi() ? "Available" : "Not Available");
        binding.guideTxt.setText(object.isGuide() ? "Available" : "Not Available");

        binding.tourDesc.setText(object.getDescription());
        binding.priceTxt.setText("$" + object.getPrice());
    }

    private void getIntentExtra() {
        object = (Tour) getIntent().getSerializableExtra("object");
    }
}
