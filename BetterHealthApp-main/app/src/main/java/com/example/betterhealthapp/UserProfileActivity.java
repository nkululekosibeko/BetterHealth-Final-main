package com.example.betterhealthapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private EditText homeAddressEditText, nextOfKinEditText, nextOfKinNumEditText, phoneNumberEditText;
    private Button updateButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get references to EditText fields
        homeAddressEditText = findViewById(R.id.Home_Address_Sign_Up_TextEdit);
        nextOfKinEditText = findViewById(R.id.Next_of_kin_Sign_Up_TextEdit);
        nextOfKinNumEditText = findViewById(R.id.NextOfKinNum_Sign_Up_TextEdit);
        phoneNumberEditText = findViewById(R.id.My_Phone_Number_Sign_Up_TextEdit);

        // Get reference to the update button
        updateButton = findViewById(R.id.Next_btn_Sign_Up);

        // Set click listener for the update button
        updateButton.setOnClickListener(v -> updateUserProfile());

        // Fetch and populate user profile data
        fetchAndPopulateUserProfile();
    }

    private void fetchAndPopulateUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("userInfo").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserProfileInfo userProfileInfo = dataSnapshot.getValue(UserProfileInfo.class);
                        if (userProfileInfo != null) {
                            homeAddressEditText.setText(userProfileInfo.getAddress());
                            nextOfKinEditText.setText(userProfileInfo.getNextOfKinName());
                            nextOfKinNumEditText.setText(userProfileInfo.getNextOfKinPhoneNumber());
                            phoneNumberEditText.setText(userProfileInfo.getPhoneNumber());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    private void updateUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String homeAddress = homeAddressEditText.getText().toString();
            String nextOfKin = nextOfKinEditText.getText().toString();
            String nextOfKinNum = nextOfKinNumEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();

            UserProfileInfo userProfileInfo = new UserProfileInfo();
            userProfileInfo.setAddress(homeAddress);
            userProfileInfo.setNextOfKinName(nextOfKin);
            userProfileInfo.setNextOfKinPhoneNumber(nextOfKinNum);
            userProfileInfo.setPhoneNumber(phoneNumber);

            mDatabase.child("userInfo").child(userId).setValue(userProfileInfo)
                    .addOnSuccessListener(aVoid -> {
                        // Data successfully updated
                        Toast.makeText(UserProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Failed to update data
                        Toast.makeText(UserProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
