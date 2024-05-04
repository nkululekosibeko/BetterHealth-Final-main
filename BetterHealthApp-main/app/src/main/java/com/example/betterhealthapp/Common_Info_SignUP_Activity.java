package com.example.betterhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Common_Info_SignUP_Activity extends AppCompatActivity {

    private EditText fNameEditText, HomeAddress, lastNameEditText, nextOfKinEditText,
            nextOfKinNumEditText, idNumEditText, contactNumEditText;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_common_info_sign_up);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get references to EditText fields
        fNameEditText = findViewById(R.id.fName_Sign_Up_TextEdit);
        lastNameEditText = findViewById(R.id.Last_Name_Sign_Up_TextEdit);
        HomeAddress = findViewById(R.id.Home_Address_Sign_Up_TextEdit);
        nextOfKinNumEditText = findViewById(R.id.NextOfKinNum_Sign_Up_TextEdit);
        nextOfKinEditText = findViewById(R.id.Next_of_kin_Sign_Up_TextEdit);
        idNumEditText = findViewById(R.id.IDNum_Sign_Up_TextEdit);
        contactNumEditText = findViewById(R.id.My_Phone_Number_Sign_Up_TextEdit);

        // Get reference to the sign-up button
        Button nextButton = findViewById(R.id.Next_btn_Sign_Up);

        // Set click listener for the next button
        nextButton.setOnClickListener(v -> {
            // Get the current user
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // Get the current user's ID and email
                String userId = currentUser.getUid();
                String userEmail = currentUser.getEmail();

                // Get the values entered by the user
                String fullName = fNameEditText.getText().toString();
                String phoneNumber = contactNumEditText.getText().toString();
                String nextOfKinName = nextOfKinEditText.getText().toString();
                String nextOfKinPhoneNumber = nextOfKinNumEditText.getText().toString();
                String address = HomeAddress.getText().toString();
                String surname = lastNameEditText.getText().toString();
                String idNumber = idNumEditText.getText().toString();

                // Check if any of the fields are empty
                if (fullName.isEmpty() || phoneNumber.isEmpty() || nextOfKinName.isEmpty() ||
                        nextOfKinPhoneNumber.isEmpty() || address.isEmpty() || surname.isEmpty() ||
                        idNumber.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return; // Exit the method early
                }

                // Create UserProfileInfo object
                UserProfileInfo userProfileInfo = new UserProfileInfo(
                        userId,
                        fullName,
                        userEmail,
                        phoneNumber,
                        nextOfKinName,
                        nextOfKinPhoneNumber,
                        address,
                        surname,
                        idNumber
                );

                // Store UserProfileInfo object in Firebase Realtime Database
                mDatabase.child("userInfo").child(userId).setValue(userProfileInfo)
                        .addOnSuccessListener(aVoid -> {
                            // Data successfully stored
                            Toast.makeText(Common_Info_SignUP_Activity.this, "Details Uploaded", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Common_Info_SignUP_Activity.this, StudentDashboardActivity.class);
                            startActivity(intent);
                            finish();
                            // You can add further actions here like navigating to another activity
                        })
                        .addOnFailureListener(e -> {
                            // Failed to store data
                            // Handle failure, log error, show message, etc.
                            String errorMessage = e.getMessage();
                            Log.e("Firebase Upload Error", errorMessage); // Log the error message for debugging
                            Toast.makeText(Common_Info_SignUP_Activity.this, "Failed to upload details: " + errorMessage, Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}


